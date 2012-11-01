/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

/**
 * Implementation of <code>AttacherMgr</code>.
 * 
 * @author Andrey Markelov
 */
public class AttacherMgrImpl
    implements AttacherMgr
{
    /**
     * PlugIn key.
     */
    private static final String PLUGIN_KEY = "PLUGIN_ATTACH_DELETER";

    /**
     * Projects.
     */
    private static final String PROJECTS = "PROJECTS";

    /**
     * Value separator.
     */
    private static final String VAL_SEPARATOR = "&";

    /**
     * Plugin factory.
     */
    private final PluginSettingsFactory pluginSettingsFactory;

    /**
     * Constructor.
     */
    public AttacherMgrImpl(
        PluginSettingsFactory pluginSettingsFactory)
    {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public String[] getProjectKeys()
    {
        List<String> projectKeys = new ArrayList<String>();

        String val = getStringProperty(PROJECTS);
        if (val != null && val.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(val, VAL_SEPARATOR);
            while(st.hasMoreTokens())
            {
                projectKeys.add(st.nextToken());
            }
        }

        return projectKeys.toArray(new String[projectKeys.size()]);
    }

    private String getStringProperty(String key)
    {
        return (String) pluginSettingsFactory.createSettingsForKey(PLUGIN_KEY).get(key);
    }

    @Override
    public void setProjectKeys(String[] projectKeys)
    {
        StringBuilder sb = new StringBuilder();
        if (projectKeys != null)
        {
            for (String projectKey : projectKeys)
            {
                sb.append(projectKey).append(VAL_SEPARATOR);
            }
        }

        setStringProperty(PROJECTS, sb.toString());
    }

    private void setStringProperty(String key, String value)
    {
        pluginSettingsFactory.createSettingsForKey(PLUGIN_KEY).put(key, value);
    }
}
