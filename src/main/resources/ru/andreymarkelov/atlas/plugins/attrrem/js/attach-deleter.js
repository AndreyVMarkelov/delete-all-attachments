AJS.$(function($) {
    var initDialogTriggers = function(context) {
        AJS.$(".issueaction-delete-all-attachments", context || document.body).each(function() {
            new JIRA.FormDialog({
                trigger: this,
                id: this.id + '-dialog',
                ajaxOptions: {
                    url: this.href,
                    data: { decorator: 'dialog', inline: 'true' }
                }
            });
        });
    };
    JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (event, context, reason) {
        initDialogTriggers(context);
    });
    initDialogTriggers();
});
