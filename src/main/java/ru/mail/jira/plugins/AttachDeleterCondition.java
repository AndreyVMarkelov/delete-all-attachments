/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

import java.util.Collection;
import java.util.Map;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.attachment.Attachment;
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
        final Map<String, Object> params = jiraHelper.getContextParams();
        final Issue issue = (Issue) params.get("issue");
        if (project != null && issue != null)
        {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null)
            {
                Collection<Attachment> atts = issue.getAttachments();
                if (atts == null || atts.isEmpty())
                {
                    return false;
                }

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
