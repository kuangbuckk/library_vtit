package com.project.library.controllers;

import com.project.library.constants.FilenameTemplate;
import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.LoginDTO;
import com.project.library.dtos.UserDTO;
import com.project.library.dtos.search.UserSearchDTO;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.LoginResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.UserService;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<UserPageResponse>> getAllUsers(
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestBody UserSearchDTO userSearchDTO,
            HttpServletRequest httpServletRequest
    ) {
        UserPageResponse userPageResponse = userService.getUsers(pageNumber, size, userSearchDTO);
        return ResponseUtil.success(
                MessageKeys.GET_USER_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.GET_USER_SUCCESSFULLY),
                userPageResponse
        );
    }

    @GetMapping("/excel")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> getUserExcelReport(
            HttpServletRequest httpServletRequest
    ) {
        byte[] excelUserData = userService.exportUserExcelData();
        return ResponseUtil.download(FilenameTemplate.USER_EXCEL_NAME, excelUserData);
    }

//    @GetMapping("/{id}")
//    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
//    public ResponseEntity<GenericResponse> getUserByCode(
//            @PathVariable Long id,
//            HttpServletRequest httpServletRequest
//    ) {
//        UserResponse userResponse = userService.getUserByCode(id);
//        return ResponseEntity.ok(GenericResponse.success(userResponse));
//    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserDTO userDTO) {
        UserResponse userResponse = userService.createUser(userDTO);
        return ResponseUtil.success(
                MessageKeys.REGISTER_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY),
                userResponse
        );
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<String>> loginUser(
            @RequestBody @Valid LoginDTO loginDTO,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        String refreshToken = loginResponse.getRefreshToken();
        ResponseCookie cookie = ResponseCookie.from("x-auth-refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseUtil.success(MessageKeys.LOGIN_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY),
                loginResponse.getToken()
        );
    }

    /**
     * Hàm này thực hiện gửi yêu cầu refresh lại access token trả về cho client bằng refresh token.
     *
     * @param {String} refreshTokens - Refresh token được lấy từ client (ở đây sẽ là Cookie,
     *                 có thể là Header hoặc localStorage).
     * @return {String} - Access Token.
     * <p>
     * Ở phía Client:
     * - Khi nhận được access token mới, client sẽ lưu lại access token mới và sử dụng nó để gọi API.
     * - Nếu access token hết hạn, server sẽ trả về Unauthorized và Client sẽ có cơ chế redirect gửi
     * yêu cầu tới /refresh token lên server để lấy access token mới rồi intercept vào API vừa gọi.
     * - Nếu refresh token hết hạn, server sẽ trả về Unauthorized và Client sẽ bị yêu cầu đăng nhập lại.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<GenericResponse<String>> refreshToken(
            @CookieValue(name = "x-auth-refresh-token") String refreshToken,
            HttpServletResponse response
    ) throws Exception {
        LoginResponse loginResponse = userService.refreshToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from("x-auth-refresh-token", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseUtil.success(MessageKeys.LOGIN_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY),
                loginResponse.getToken()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<String>> logout(
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
        return ResponseUtil.success(
                MessageKeys.LOGOUT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.LOGOUT_SUCCESSFULLY),
                MessageKeys.LOGOUT_SUCCESSFULLY
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<UserResponse>> updateUser(
            @RequestBody @Valid UserDTO userDTO,
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {

        UserResponse userResponse = userService.updateUser(userDTO, id);
        return ResponseUtil.success(MessageKeys.UPDATE_USER_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_USER_SUCCESSFULLY),
                userResponse
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<UserResponse>> deleteUser(
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseUtil.success(MessageKeys.DELETE_USER_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_USER_SUCCESSFULLY),
                userService.deleteUser(id)
        );
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<Long>> destroyUser(
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        userService.destroyUser(id);
        return ResponseUtil.success(MessageKeys.DESTROY_USER_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DESTROY_USER_SUCCESSFULLY),
                id
        );
    }
}
