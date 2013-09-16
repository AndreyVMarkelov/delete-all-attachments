package ru.andreymarkelov.atlas.plugins.attrrem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.ApplicationProperties;

public class AttachDeleterConfig extends JiraWebActionSupport {
    private static final long serialVersionUID = -8881690781888724545L;

    private final ApplicationProperties applicationProperties;
    private final AttacherMgr attacherMgr;
    private boolean isSaved = false;
    private ProjectManager prMgr;
    private List<String> savedProjKeys;
    private String[] selectedProjKeys = new String[0];

    public AttachDeleterConfig(ApplicationProperties applicationProperties, AttacherMgr attacherMgr, ProjectManager prMgr) {
        this.applicationProperties = applicationProperties;
        this.attacherMgr = attacherMgr;
        this.prMgr = prMgr;
        selectedProjKeys = attacherMgr.getProjectKeys();
        savedProjKeys = selectedProjKeys == null ? null : Arrays.asList(selectedProjKeys);
    }

    @Override
    public String doDefault() throws Exception {
        if(!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        return INPUT;
    }

	@Override
    @com.atlassian.jira.security.xsrf.RequiresXsrfCheck
    protected String doExecute() throws Exception {
        attacherMgr.setProjectKeys(selectedProjKeys);
        if (selectedProjKeys != null) {
            savedProjKeys = Arrays.asList(attacherMgr.getProjectKeys());
        }
        setSaved(true);
        return getRedirect("AttachDeleterConfig!default.jspa?saved=true");
    }

    public Map<String, String> getAllProjects() {
        Map<String, String> allProjs = new TreeMap<String, String>();
        List<Project> projs = prMgr.getProjectObjects();
        if (projs != null) {
            for (Project proj : projs) {
                allProjs.put(proj.getId().toString(), getProjView(proj.getKey(), proj.getName()));
            }
        }
        return allProjs;
    }

    public String getBaseUrl() {
        return applicationProperties.getBaseUrl();
    }

    private String getProjView(String key, String name) {
        return (key + ": " + name);
    }

    public List<String> getSavedProjKeys() {
        return savedProjKeys;
    }

    public String[] getSelectedProjKeys() {
        return selectedProjKeys;
    }

    public boolean hasAdminPermission() {
        User user = getLoggedInUser();
        if (user == null) {
            return false;
        }
        return getPermissionManager().hasPermission(Permissions.ADMINISTER, getLoggedInUser());
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public void setSavedProjKeys(List<String> savedProjKeys) {
        this.savedProjKeys = savedProjKeys;
    }

    public void setSelectedProjKeys(String[] selectedProjKeys) {
        this.selectedProjKeys = selectedProjKeys;
    }
}
