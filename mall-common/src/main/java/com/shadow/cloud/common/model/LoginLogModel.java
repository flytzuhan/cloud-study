package com.shadow.cloud.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginLogModel implements Serializable {

    private static final long serialVersionUID = -1943612754213388528L;

    private String userId;

    private String username;

    private String uniqueId;

    private String loginTime;

    private String loginStatus;

    private String country;
}
