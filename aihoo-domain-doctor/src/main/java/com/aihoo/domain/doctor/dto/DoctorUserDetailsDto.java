package com.aihoo.domain.doctor.dto;

import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医生详情 Dto（合并 DoctorUser + DoctorVisitSet）。
 * 由 service 返回，controller 负责转换为 api 层 vo。
 *
 * @author mcp
 * @since 2026-06-15
 */
@Data
public class DoctorUserDetailsDto {

    public static DoctorUserDetailsDto of(DoctorUser user, DoctorVisitSet visitSet) {
        DoctorUserDetailsDto dto = new DoctorUserDetailsDto();
        if (user != null) {
            dto.setId(user.getId());
            dto.setHeadImg(user.getHeadImg());
            dto.setName(user.getName());
            dto.setTag(user.getTag());
            dto.setMemberNum(user.getMemberNum());
            dto.setHospitalId(user.getHospitalId());
            dto.setHospitalName(user.getHospitalName());
            dto.setDepartId(user.getDepartId());
            dto.setDepartCode(user.getDepartCode());
            dto.setDepartName(user.getDepartName());
            dto.setOfficeHolderCode(user.getOfficeHolderCode());
            dto.setOfficeHolderName(user.getOfficeHolderName());
            dto.setBeGoodAtText(user.getBeGoodAtText());
            dto.setIntroductionText(user.getIntroductionText());
            dto.setPersonTypeCode(user.getPersonTypeCode());
            dto.setPersonTypeName(user.getPersonTypeName());
            dto.setPositionCode(user.getPositionCode());
            dto.setPositionName(user.getPositionName());
            dto.setPapersCode(user.getPapersCode());
            dto.setPapersName(user.getPapersName());
            dto.setPapersNumbers(user.getPapersNumbers());
            dto.setStatus(user.getStatus());
        }
        if (visitSet != null) {
            dto.setIsImg(visitSet.getIsImg());
            dto.setImgPrice(visitSet.getImgPrice());
            dto.setUpperLimit(visitSet.getUpperLimit());
            dto.setIsDisturb(visitSet.getIsDisturb());
            dto.setNoDisturbTime(visitSet.getNoDisturbTime());
        }
        return dto;
    }

    @Schema(name = "id", description = "主键ID")
    private String id;

    @Schema(name = "headImg", description = "头像")
    private String headImg;

    @Schema(name = "name", description = "姓名")
    private String name;

    @Schema(name = "tag", description = "标签")
    private String tag;

    @Schema(name = "memberNum", description = "工号")
    private String memberNum;

    @Schema(name = "hospitalId", description = "医院id")
    private String hospitalId;

    @Schema(name = "hospitalName", description = "就职医院")
    private String hospitalName;

    @Schema(name = "departId", description = "科室id")
    private String departId;

    @Schema(name = "departCode", description = "科室编码")
    private String departCode;

    @Schema(name = "departName", description = "所在科室")
    private String departName;

    @Schema(name = "officeHolderCode", description = "职称编码")
    private String officeHolderCode;

    @Schema(name = "officeHolderName", description = "职称")
    private String officeHolderName;

    @Schema(name = "beGoodAtText", description = "擅长")
    private String beGoodAtText;

    @Schema(name = "achievement", description = "成就")
    private String achievement;

    @Schema(name = "introductionText", description = "简介")
    private String introductionText;

    @Schema(name = "personTypeCode", description = "人员类别编码")
    private String personTypeCode;

    @Schema(name = "personTypeName", description = "人员类别")
    private String personTypeName;

    @Schema(name = "positionCode", description = "职务编码")
    private String positionCode;

    @Schema(name = "positionName", description = "职务")
    private String positionName;

    @Schema(name = "papersCode", description = "证件类型编码")
    private String papersCode;

    @Schema(name = "papersName", description = "证件")
    private String papersName;

    @Schema(name = "papersNumbers", description = "证件号码")
    private String papersNumbers;

    @Schema(name = "sex", description = "医生性别")
    private String sex;

    @Schema(name = "status", description = "状态")
    private String status;

    @Schema(name = "isImg", description = "是否开启图文问诊 0-未开 1-开启")
    private Integer isImg;

    @Schema(name = "imgPrice", description = "图文问诊价格")
    private Integer imgPrice;

    @Schema(name = "upperLimit", description = "接单上限")
    private Integer upperLimit;

    @Schema(name = "isDisturb", description = "是否开启免打扰时段")
    private Integer isDisturb;

    @Schema(name = "noDisturbTime", description = "免打扰时段")
    private String noDisturbTime;
}
