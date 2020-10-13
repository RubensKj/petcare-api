package br.com.ipet.Payload;

import br.com.ipet.Models.Address;
import br.com.ipet.Models.Role;
import br.com.ipet.Models.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long iduser;
    private String email;
    private Set<Role> roles = new HashSet<>();
    private String completeName;
    private String cpf;
    private String phoneNumber;
    private Address address;
    private Set<Long> favorites = new HashSet<>();
    private Set<Long> orders;

    private String avatar;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime joinedDate;

    public UserDTO() {
    }

    public UserDTO(Long iduser, String email, Set<Role> roles, String completeName, String cpf, String phoneNumber, Address address, Set<Long> favorites, Set<Long> orders, String avatar, LocalDateTime joinedDate) {
        this.iduser = iduser;
        this.email = email;
        this.roles = roles;
        this.completeName = completeName;
        this.cpf = cpf;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.favorites = favorites;
        this.orders = orders;
        this.avatar = avatar;
        this.joinedDate = joinedDate;
    }

    public static UserDTO of(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getIduser(),
                user.getEmail(),
                user.getRoles(),
                user.getCompleteName(),
                user.getCpf(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getFavorites(),
                user.getOrders(),
                user.getAvatar(),
                user.getJoinedDate()
        );
    }

    public Long getIduser() {
        return iduser;
    }

    public void setIduser(Long iduser) {
        this.iduser = iduser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Long> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Long> favorites) {
        this.favorites = favorites;
    }

    public Set<Long> getOrders() {
        return orders;
    }

    public void setOrders(Set<Long> orders) {
        this.orders = orders;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }
}
