package ru.andreymarkelov.atlas.plugins.attrrem.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class AttacherMgrImpl implements AttacherMgr {
    private static final String PLUGIN_KEY = "PLUGIN_ATTACH_DELETER";
    private static final String PROJECTS = "PROJECTS";
    private static final String ALL_ACTIVE = "ALL_ACTIVE";
    private static final String VAL_SEPARATOR = "&";

    private final PluginSettingsFactory pluginSettingsFactory;

    public AttacherMgrImpl(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public String[] getProjectKeys() {
        List<String> projectKeys = new ArrayList<>();
        String val = getStringProperty(PROJECTS);
        if (val != null && val.length() > 0) {
            StringTokenizer st = new StringTokenizer(val, VAL_SEPARATOR);
            while(st.hasMoreTokens()) {
                projectKeys.add(st.nextToken());
            }
        }
        return projectKeys.toArray(new String[projectKeys.size()]);
    }

    @Override
    public void setProjectKeys(String[] projectKeys) {
        StringBuilder sb = new StringBuilder();
        if (projectKeys != null) {
            for (String projectKey : projectKeys) {
                sb.append(projectKey).append(VAL_SEPARATOR);
            }
        }
        setStringProperty(PROJECTS, sb.toString());
    }

    @Override
    public boolean isActiveForAll() {
        String activeForAll = getStringProperty(ALL_ACTIVE);
        return Boolean.valueOf(activeForAll);
    }

    @Override
    public void setActiveForAll(boolean activeForAll) {
        setStringProperty(ALL_ACTIVE, Boolean.toString(activeForAll));
    }

    private String getStringProperty(String key) {
        return (String) pluginSettingsFactory.createSettingsForKey(PLUGIN_KEY).get(key);
    }

    private void setStringProperty(String key, String value) {
        pluginSettingsFactory.createSettingsForKey(PLUGIN_KEY).put(key, value);
    }
}
