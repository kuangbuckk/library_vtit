package com.project.library.security;
import com.project.library.configurations.RoleConfig;
import com.project.library.entities.User;
import com.project.library.responses.PostResponse;
import com.project.library.services.interfaces.IPostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final RoleConfig roleConfig;
    private final IPostService postService;

    public boolean fileRole(HttpServletRequest request) {
        System.out.println("Evaluating fileRole for: " + request.getRequestURI());
        String uri = request.getRequestURI();
        String requiredPermission = roleConfig.getFunctionByUri(uri);

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roleGroup -> roleConfig.roleGroupHasFunction(roleGroup, requiredPermission));
    }

    public boolean isPostOwner(UUID postCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        PostResponse existingPost = postService.getPostByCode(postCode);
        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority ->
            grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
            grantedAuthority.getAuthority().equals("ROLE_MANAGER"))
        ) {
            return true;
        }
        return existingPost.getAuditor().getCreatedBy()
                .equals(((User) authentication.getPrincipal()).getUsername());
    }

}