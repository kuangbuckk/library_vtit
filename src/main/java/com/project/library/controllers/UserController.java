package com.project.library.controllers;

import com.project.library.components.JwtTokenUtils;
import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.LoginDTO;
import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.LoginResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.interfaces.IUserService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> getAllUsers(
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            HttpServletRequest httpServletRequest
    ) {
        UserPageResponse userPageResponse = userService.getUsers(pageNumber, size, keyword);
        return ResponseEntity.ok(GenericResponse.success(userPageResponse));
    }
    
    @GetMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> getUserByCode(
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        UserResponse userResponse = userService.getUserByCode(code);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserDTO userDTO) {
        UserResponse userResponse = userService.createUser(userDTO);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> loginUser(
            @RequestBody @Valid LoginDTO loginDTO,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        String refreshToken = loginResponse.getRefreshToken();
//        Cookie cookie = new Cookie("x-auth-refresh-token", refreshToken);
//        cookie.setHttpOnly(true);
////        cookie.setPath("/");
////        cookie.setMaxAge(60 * 60 * 24 * 30);
//        response.addCookie(cookie);
        ResponseCookie cookie = ResponseCookie.from("x-auth-refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.LOGIN_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY),
                loginResponse.getToken()));
    }

    /**
     * Hàm này thực hiện gửi yêu cầu refresh lại access token trả về cho client bằng refresh token.
     * @param {String} refreshTokens - Refresh token được lấy từ client (ở đây sẽ là Cookie,
     * có thể là Header hoặc localStorage).
     * @returns {String} - Access Token.
     *
     * Ở phía Client:
     * - Khi nhận được access token mới, client sẽ lưu lại access token mới và sử dụng nó để gọi API.
     * - Nếu access token hết hạn, server sẽ trả về Unauthorized và Client sẽ có cơ chế redirect gửi
     * yêu cầu tới /refresh token lên server để lấy access token mới rồi intercept vào API vừa gọi.
     * - Nếu refresh token hết hạn, server sẽ trả về Unauthorized và Client sẽ bị yêu cầu đăng nhập lại.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<GenericResponse> refreshToken(
            @CookieValue(name = "x-auth-refresh-token") String refreshToken
    ) {
        String accessToken = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(GenericResponse.success(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse> logout(
            @CookieValue(name = "x-auth-refresh-token") String refreshToken,
            HttpServletResponse response
    ) {
        userService.logout(refreshToken);
        ResponseCookie responseCookie = ResponseCookie.from("x-auth-refresh-token", null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        response.setHeader("Set-Cookie", responseCookie.toString());
        return ResponseEntity.ok(GenericResponse.success("Logout successfully"));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> updateUser(
            @RequestBody @Valid UserDTO userDTO,
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {

        UserResponse userResponse = userService.updateUser(userDTO, code);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> deleteUser(
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        userService.deleteUser(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> destroyUser(
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        userService.destroyUser(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
