package ru.mail.jira.plugins;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractJiraPermissionCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.webtests.Permissions;
import com.opensymphony.user.User;

public class AttachDeleterCondition
    extends AbstractJiraPermissionCondition
{
    /**
     * Constructor.
     */
    public AttachDeleterCondition(PermissionManager permissionManager)
    {
        super(permissionManager);
    }

    @Override
    public boolean shouldDisplay(User user, JiraHelper jiraHelper)
    {
        final Project project = jiraHelper.getProjectObject();
        boolean d = permissionManager.hasPermission(Permissions.ATTACHMENT_DELETE_ALL, project, user);
        return false;
    }
}
