package br.com.ipet.Controllers.File;

import br.com.ipet.Models.User;
import br.com.ipet.Services.FileStorageService;
import br.com.ipet.Services.UserAuthPrincipal;
import br.com.ipet.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://aw-petcare-client.herokuapp.com",
        "https://aw-petcare-business.herokuapp.com",
        "http://aw-petcare-client.herokuapp.com",
        "http://aw-petcare-business.herokuapp.com",
        "https://petcare-client.now.sh/",
        "https://petcare-client.now.sh"
})
@RestController
@RequestMapping("/")
public class FileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, Authentication authentication, HttpServletRequest req) {
        String fileName = fileStorageService.storeFile(file);
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        URI contextUrl = URI.create(req.getRequestURL().toString()).resolve(req.getContextPath());

        String urlImage = contextUrl + "images/" + resource.getFilename();

        UserAuthPrincipal user = (UserAuthPrincipal) authentication.getPrincipal();
        User user1 = userService.findByEmail(user.getUsername());

        user1.setAvatar(urlImage);

        userService.save(user1);

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

    public static String saveImage(MultipartFile file, HttpServletRequest req, String removeContext, FileStorageService fileStorageService) {
        String fileName = fileStorageService.storeFile(file);
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        URI contextUrl = URI.create(req.getRequestURL().toString()).resolve(req.getContextPath());
        String urlImage = contextUrl + "images/" + resource.getFilename();
        if (urlImage.contains(removeContext)) {
            return urlImage.replace(removeContext, "");
        } else {
            return urlImage;
        }
    }
}
