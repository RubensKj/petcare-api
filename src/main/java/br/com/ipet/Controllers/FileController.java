package br.com.ipet.Controllers;

import br.com.ipet.Services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.25.17:3000" })
@RestController
@RequestMapping("/")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest req)
            throws IOException {
        String fileName = fileStorageService.storeFile(file);
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        URI contextUrl = URI.create(req.getRequestURL().toString()).resolve(req.getContextPath());

        String urlImage = contextUrl + "images/" + resource.getFilename();

        System.out.println(urlImage);

        return ResponseEntity.ok().body(urlImage);
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = fileStorageService.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
