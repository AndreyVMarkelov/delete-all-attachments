/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

/**
 * Configuration manager of plugin.
 * 
 * @author Andrey Markelov
 */
public interface AttacherMgr
{
    /**
     * Get a collection of project keys.
     */
    String[] getProjectKeys();

    /**
     * Set a collection of project keys.
     */
    void setProjectKeys(String[] projectKeys);
}
