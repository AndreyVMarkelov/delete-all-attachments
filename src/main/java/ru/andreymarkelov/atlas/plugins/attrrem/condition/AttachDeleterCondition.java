package ru.andreymarkelov.atlas.plugins.attrrem.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import ru.andreymarkelov.atlas.plugins.attrrem.manager.AttacherMgr;

import static com.atlassian.jira.permission.ProjectPermissions.DELETE_ALL_ATTACHMENTS;

public class AttachDeleterCondition extends AbstractIssueWebCondition {
    private final AttacherMgr attacherMgr;
    private final PermissionManager permissionManager;

    public AttachDeleterCondition(PermissionManager permissionManager, AttacherMgr attacherMgr) {
        this.permissionManager = permissionManager;
        this.attacherMgr = attacherMgr;
    }

    public boolean shouldDisplay(ApplicationUser user, Issue issue, JiraHelper jiraHelper) {
        if (attacherMgr.isActiveForAll()) {
            return true;
        }

        if (issue != null && issue.getKey() != null) {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null) {
                for (String projectKey : projectKeys) {
                    if (projectKey.equals(issue.getProjectObject().getId().toString())) {
                        return permissionManager.hasPermission(DELETE_ALL_ATTACHMENTS, issue, user);
                    }
                }
            }
        }
        return false;
    }
}
