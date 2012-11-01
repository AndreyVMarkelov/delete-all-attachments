/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractJiraPermissionCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.webtests.Permissions;
import com.opensymphony.user.User;

/**
 * Condition for action "delete all attachments".
 * 
 * @author Andrey Markelov
 */
public class AttachDeleterCondition
    extends AbstractJiraPermissionCondition
{
    /**
     * Plug-In data manager.
     */
    private final AttacherMgr attacherMgr;

    /**
     * Constructor.
     */
    public AttachDeleterCondition(
        PermissionManager permissionManager,
        AttacherMgr attacherMgr)
    {
        super(permissionManager);
        this.attacherMgr = attacherMgr;
    }

    @Override
    public boolean shouldDisplay(
        User user,
        JiraHelper jiraHelper)
    {
        final Project project = jiraHelper.getProjectObject();
        if (project != null)
        {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null)
            {
                for (String projectKey : projectKeys)
                {
                    if (projectKey.equals(project.getId().toString()))
                    {
                        return permissionManager.hasPermission(Permissions.ATTACHMENT_DELETE_ALL, project, user);
                    }
                }
            }
        }

        return false;
    }
}
