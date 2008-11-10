/*
 * Copyright 2004-2008 Malcolm A. Edgar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click;

import net.sf.click.service.ModuleConfigService;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bob Schellink
 */
public class ModuleContext extends Context {

    public ModuleContext(ServletContext context, ServletConfig config,
        HttpServletRequest request, HttpServletResponse response, boolean isPost,
        ClickServlet clickServlet) {
        super(context, config, request, response, isPost, clickServlet);
    }

    public String getModuleRoot(String moduleName) {
        Map modules = getModuleConfigService().getModuleService().getModules();
        ClickModule module = (ClickModule) modules.get(moduleName);
        if (module != null) {
            return module.getModulePath();
        }
        return "";
    }

    private ModuleConfigService getModuleConfigService() {
        return (ModuleConfigService) clickServlet.getConfigService();
    }
}