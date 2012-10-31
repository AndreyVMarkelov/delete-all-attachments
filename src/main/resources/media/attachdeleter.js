jQuery(document).ready(function() {
    jQuery('#mail_ru_delete_all_attachments').bind('click', function(event) {
        event.preventDefault();
        if(confirm(AJS.I18n.getText("attachdelete.confirm"))) {
            window.location = jQuery('#mail_ru_delete_all_attachments').attr("href");
        }
  });
});
