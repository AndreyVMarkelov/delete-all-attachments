package ru.andreymarkelov.atlas.plugins.attrrem;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;

public class AttachDeleterCondition extends AbstractIssueCondition {
    private final AttacherMgr attacherMgr;
    private final PermissionManager permissionManager;

    public AttachDeleterCondition(PermissionManager permissionManager, AttacherMgr attacherMgr) {
        this.permissionManager = permissionManager;
        this.attacherMgr = attacherMgr;
    }

    public boolean shouldDisplay(User user, Issue issue, JiraHelper jh) {
        if (issue != null && issue.getKey() != null) {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null) {
                for (String projectKey : projectKeys) {
                    if (projectKey.equals(issue.getProjectObject().getId().toString())) {
                        return permissionManager.hasPermission(Permissions.ATTACHMENT_DELETE_ALL, issue.getProjectObject(), user);
                    }
                }
            }
        }

        return false;
    }
}
