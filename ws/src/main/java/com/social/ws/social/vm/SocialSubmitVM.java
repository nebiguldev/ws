package com.social.ws.social.vm;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SocialSubmitVM {

    @Size(min=1, max=1000)
    private String content;

    private long attachmentId;

}