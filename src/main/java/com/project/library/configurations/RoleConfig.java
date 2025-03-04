package com.project.library.configurations;

import com.project.library.entities.Function;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.utils.UrlFormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class RoleConfig {
    private final Map<String, String> roles = new HashMap<>();
    private final RoleGroupRepository roleGroupRepository;

    public RoleConfig(@Value("classpath:role.properties") Resource resource, RoleGroupRepository roleGroupRepository) throws IOException {
        this.roleGroupRepository = roleGroupRepository;
        Properties properties = new Properties();
        properties.load(resource.getInputStream());

        for (String key : properties.stringPropertyNames()) {
            roles.put(key, properties.getProperty(key));
//            System.out.println(key + " = " + properties.getProperty(key));
        }
    }

    public String getFunctionByUri(String uri) {
        String formattedUri = UrlFormatUtil.formatUrl(uri);
        return roles.getOrDefault(formattedUri, "");
    }

    public boolean roleGroupHasFunction(String roleGroupName, String functionName) {
        List<String> functions = roleGroupRepository
                .findFunctionsByRoleGroupName(roleGroupName.replace("ROLE_", ""))
                .stream()
                .map(Function::getFunctionName)
                .toList();
        return functions.contains(functionName);
    }
}
