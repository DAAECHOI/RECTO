package com.ssafy.recto.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
@ApiModel(value = "Gift : 사진 선물")
public class Gift {


    @ApiModelProperty(value = "선물 seq")
    private int gift_seq;

    @ApiModelProperty(value = "발신인 seq")
    private String gift_from;

    @ApiModelProperty(value = "사진 seq")
    private int photo_seq;

    @ApiModelProperty(value = "수신인 seq")
    private String gift_to;

    @ApiModelProperty(value = "사진 식별 ID")
    public String photo_id;

    @ApiModelProperty(value = "사진 주소")
    public String photo_url;

    @ApiModelProperty(value = "동영상 주소")
    public String video_url;

    @ApiModelProperty(value = "문구")
    public String phrase;

    @ApiModelProperty(value = "사진 비밀번호")
    public String photo_pwd;

    @ApiModelProperty(value = "포토카드 디자인")
    public int design;

}
