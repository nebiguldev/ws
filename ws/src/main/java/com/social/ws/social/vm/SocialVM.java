package com.social.ws.social.vm;

import com.social.ws.file.vm.FileAttachmentVM;
import com.social.ws.social.Social;
import com.social.ws.user.vm.UserVM;
import lombok.Data;

@Data
public class SocialVM {

    private long id;

    private String content;

    private long timestamp;

    private UserVM user;

    private FileAttachmentVM fileAttachment;

    public SocialVM(Social social) {
        this.setId(social.getId());
        this.setContent(social.getContent());
        this.setTimestamp(social.getTimestamp().getTime());
        this.setUser(new UserVM(social.getUser()));
        if(social.getFileAttachment() != null) {
            this.fileAttachment = new FileAttachmentVM(social.getFileAttachment());
        }
    }
}
