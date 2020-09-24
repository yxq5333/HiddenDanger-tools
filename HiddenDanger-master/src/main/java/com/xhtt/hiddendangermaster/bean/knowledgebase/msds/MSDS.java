package com.xhtt.hiddendangermaster.bean.knowledgebase.msds;

import java.io.Serializable;

/**
 * Created by Hollow Goods on 2019-04-02.
 */
public class MSDS implements Serializable {

    private long id;
    private String chemicalsNameCn;// 中文名称
    private String chemicalsNameEn;// 英文名称
    private String nameCn2;// 中文名称2
    private String nameEn2;// 英文名称2
    private String technicalManualNo;// 计数说明书编码
    private String casno;// CAS
    private String molecularFormula;// 分子式
    private String molecularWeight;// 分子量
    private String contents;// 含量
    private String healthHazard;// 健康危害
    private String environmentHazards;// 环境危害
    private String riskExplosion;// 燃爆危险
    private String skinContact;// 皮肤接触
    private String eyeContact;// 眼睛接触
    private String inhalation;// 吸入
    private String ingestion;// 食入
    private String hazardousProperties;// 危险特性
    private String harmfulCombustionProducts;// 有害燃烧产物
    private String extinguishmentMethod;// 灭火方法
    private String emergencyHandling;// 应急处理
    private String handlingPrecautions;// 操作注意事项
    private String storePrecautions;// 储存注意事项
    private String macCn;// 中国MAC
    private String ussr;// 前苏联
    private String tlvtn;// TLVTN
    private String tlvwn; // TLVWN
    private String engineeringControl;// 工程控制
    private String respiratoryProtection;// 呼吸系统防护
    private String eyeProtection;// 眼睛防护
    private String bodyProtection;// 身体防护
    private String handProtection;// 手防护
    private String otherProtection;// 其他防护
    private String basis;// 主要成分
    private String appearanceCharacter;// 外观与性状
    private String meltingPoint;// 熔点
    private String boil;// 沸点
    private String relativeDensity;// 相对密度
    private String relativeVapourDensity;// 相对蒸气密度
    private String saturatedVaporPressure;// 饱和蒸气压
    private String combustionHeat;// 燃烧热
    private String criticalTemperature;// 临界温度
    private String criticalPressure;// 临界压力
    private String waterPartitionCoefficient;// 水分配系数的对数值
    private String flashPoint;// 闪点
    private String ignitionTemperature;// 引燃温度
    private String upperExplosiveLimit;// 爆炸上限
    private String lowExplosiveLimit;// 爆炸下限
    private String mainApplication;// 主要用途
    private String prohibitedSubstance;// 禁配物
    private String ld50;// 急性毒性LD50
    private String lc50;// 急性毒性LC50
    private String otherHarmfulEffects;// 其它有害作用
    private String wasteDisposalMethod;// 废弃物性质废弃处置方法
    private String dangerousGoodsNo;// 危险货物编号
    private String unNo;// UN编号
    private String packingGroup;// 包装类别
    private String packingMethod;// 包装方法
    private String transportConsiderations;// 运输注意事项
    private String regulatoryInformation;// 法规信息

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChemicalsNameCn() {
        return chemicalsNameCn;
    }

    public void setChemicalsNameCn(String chemicalsNameCn) {
        this.chemicalsNameCn = chemicalsNameCn;
    }

    public String getChemicalsNameEn() {
        return chemicalsNameEn;
    }

    public void setChemicalsNameEn(String chemicalsNameEn) {
        this.chemicalsNameEn = chemicalsNameEn;
    }

    public String getNameCn2() {
        return nameCn2;
    }

    public void setNameCn2(String nameCn2) {
        this.nameCn2 = nameCn2;
    }

    public String getNameEn2() {
        return nameEn2;
    }

    public void setNameEn2(String nameEn2) {
        this.nameEn2 = nameEn2;
    }

    public String getTechnicalManualNo() {
        return technicalManualNo;
    }

    public void setTechnicalManualNo(String technicalManualNo) {
        this.technicalManualNo = technicalManualNo;
    }

    public String getCasno() {
        return casno;
    }

    public void setCasno(String casno) {
        this.casno = casno;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public String getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(String molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getHealthHazard() {
        return healthHazard;
    }

    public void setHealthHazard(String healthHazard) {
        this.healthHazard = healthHazard;
    }

    public String getEnvironmentHazards() {
        return environmentHazards;
    }

    public void setEnvironmentHazards(String environmentHazards) {
        this.environmentHazards = environmentHazards;
    }

    public String getRiskExplosion() {
        return riskExplosion;
    }

    public void setRiskExplosion(String riskExplosion) {
        this.riskExplosion = riskExplosion;
    }

    public String getSkinContact() {
        return skinContact;
    }

    public void setSkinContact(String skinContact) {
        this.skinContact = skinContact;
    }

    public String getEyeContact() {
        return eyeContact;
    }

    public void setEyeContact(String eyeContact) {
        this.eyeContact = eyeContact;
    }

    public String getInhalation() {
        return inhalation;
    }

    public void setInhalation(String inhalation) {
        this.inhalation = inhalation;
    }

    public String getIngestion() {
        return ingestion;
    }

    public void setIngestion(String ingestion) {
        this.ingestion = ingestion;
    }

    public String getHazardousProperties() {
        return hazardousProperties;
    }

    public void setHazardousProperties(String hazardousProperties) {
        this.hazardousProperties = hazardousProperties;
    }

    public String getHarmfulCombustionProducts() {
        return harmfulCombustionProducts;
    }

    public void setHarmfulCombustionProducts(String harmfulCombustionProducts) {
        this.harmfulCombustionProducts = harmfulCombustionProducts;
    }

    public String getExtinguishmentMethod() {
        return extinguishmentMethod;
    }

    public void setExtinguishmentMethod(String extinguishmentMethod) {
        this.extinguishmentMethod = extinguishmentMethod;
    }

    public String getEmergencyHandling() {
        return emergencyHandling;
    }

    public void setEmergencyHandling(String emergencyHandling) {
        this.emergencyHandling = emergencyHandling;
    }

    public String getHandlingPrecautions() {
        return handlingPrecautions;
    }

    public void setHandlingPrecautions(String handlingPrecautions) {
        this.handlingPrecautions = handlingPrecautions;
    }

    public String getStorePrecautions() {
        return storePrecautions;
    }

    public void setStorePrecautions(String storePrecautions) {
        this.storePrecautions = storePrecautions;
    }

    public String getMacCn() {
        return macCn;
    }

    public void setMacCn(String macCn) {
        this.macCn = macCn;
    }

    public String getUssr() {
        return ussr;
    }

    public void setUssr(String ussr) {
        this.ussr = ussr;
    }

    public String getTlvtn() {
        return tlvtn;
    }

    public void setTlvtn(String tlvtn) {
        this.tlvtn = tlvtn;
    }

    public String getTlvwn() {
        return tlvwn;
    }

    public void setTlvwn(String tlvwn) {
        this.tlvwn = tlvwn;
    }

    public String getEngineeringControl() {
        return engineeringControl;
    }

    public void setEngineeringControl(String engineeringControl) {
        this.engineeringControl = engineeringControl;
    }

    public String getRespiratoryProtection() {
        return respiratoryProtection;
    }

    public void setRespiratoryProtection(String respiratoryProtection) {
        this.respiratoryProtection = respiratoryProtection;
    }

    public String getEyeProtection() {
        return eyeProtection;
    }

    public void setEyeProtection(String eyeProtection) {
        this.eyeProtection = eyeProtection;
    }

    public String getBodyProtection() {
        return bodyProtection;
    }

    public void setBodyProtection(String bodyProtection) {
        this.bodyProtection = bodyProtection;
    }

    public String getHandProtection() {
        return handProtection;
    }

    public void setHandProtection(String handProtection) {
        this.handProtection = handProtection;
    }

    public String getOtherProtection() {
        return otherProtection;
    }

    public void setOtherProtection(String otherProtection) {
        this.otherProtection = otherProtection;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getAppearanceCharacter() {
        return appearanceCharacter;
    }

    public void setAppearanceCharacter(String appearanceCharacter) {
        this.appearanceCharacter = appearanceCharacter;
    }

    public String getMeltingPoint() {
        return meltingPoint;
    }

    public void setMeltingPoint(String meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public String getBoil() {
        return boil;
    }

    public void setBoil(String boil) {
        this.boil = boil;
    }

    public String getRelativeDensity() {
        return relativeDensity;
    }

    public void setRelativeDensity(String relativeDensity) {
        this.relativeDensity = relativeDensity;
    }

    public String getRelativeVapourDensity() {
        return relativeVapourDensity;
    }

    public void setRelativeVapourDensity(String relativeVapourDensity) {
        this.relativeVapourDensity = relativeVapourDensity;
    }

    public String getSaturatedVaporPressure() {
        return saturatedVaporPressure;
    }

    public void setSaturatedVaporPressure(String saturatedVaporPressure) {
        this.saturatedVaporPressure = saturatedVaporPressure;
    }

    public String getCombustionHeat() {
        return combustionHeat;
    }

    public void setCombustionHeat(String combustionHeat) {
        this.combustionHeat = combustionHeat;
    }

    public String getCriticalTemperature() {
        return criticalTemperature;
    }

    public void setCriticalTemperature(String criticalTemperature) {
        this.criticalTemperature = criticalTemperature;
    }

    public String getCriticalPressure() {
        return criticalPressure;
    }

    public void setCriticalPressure(String criticalPressure) {
        this.criticalPressure = criticalPressure;
    }

    public String getWaterPartitionCoefficient() {
        return waterPartitionCoefficient;
    }

    public void setWaterPartitionCoefficient(String waterPartitionCoefficient) {
        this.waterPartitionCoefficient = waterPartitionCoefficient;
    }

    public String getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(String flashPoint) {
        this.flashPoint = flashPoint;
    }

    public String getIgnitionTemperature() {
        return ignitionTemperature;
    }

    public void setIgnitionTemperature(String ignitionTemperature) {
        this.ignitionTemperature = ignitionTemperature;
    }

    public String getUpperExplosiveLimit() {
        return upperExplosiveLimit;
    }

    public void setUpperExplosiveLimit(String upperExplosiveLimit) {
        this.upperExplosiveLimit = upperExplosiveLimit;
    }

    public String getLowExplosiveLimit() {
        return lowExplosiveLimit;
    }

    public void setLowExplosiveLimit(String lowExplosiveLimit) {
        this.lowExplosiveLimit = lowExplosiveLimit;
    }

    public String getMainApplication() {
        return mainApplication;
    }

    public void setMainApplication(String mainApplication) {
        this.mainApplication = mainApplication;
    }

    public String getProhibitedSubstance() {
        return prohibitedSubstance;
    }

    public void setProhibitedSubstance(String prohibitedSubstance) {
        this.prohibitedSubstance = prohibitedSubstance;
    }

    public String getLd50() {
        return ld50;
    }

    public void setLd50(String ld50) {
        this.ld50 = ld50;
    }

    public String getLc50() {
        return lc50;
    }

    public void setLc50(String lc50) {
        this.lc50 = lc50;
    }

    public String getOtherHarmfulEffects() {
        return otherHarmfulEffects;
    }

    public void setOtherHarmfulEffects(String otherHarmfulEffects) {
        this.otherHarmfulEffects = otherHarmfulEffects;
    }

    public String getWasteDisposalMethod() {
        return wasteDisposalMethod;
    }

    public void setWasteDisposalMethod(String wasteDisposalMethod) {
        this.wasteDisposalMethod = wasteDisposalMethod;
    }

    public String getDangerousGoodsNo() {
        return dangerousGoodsNo;
    }

    public void setDangerousGoodsNo(String dangerousGoodsNo) {
        this.dangerousGoodsNo = dangerousGoodsNo;
    }

    public String getUnNo() {
        return unNo;
    }

    public void setUnNo(String unNo) {
        this.unNo = unNo;
    }

    public String getPackingGroup() {
        return packingGroup;
    }

    public void setPackingGroup(String packingGroup) {
        this.packingGroup = packingGroup;
    }

    public String getPackingMethod() {
        return packingMethod;
    }

    public void setPackingMethod(String packingMethod) {
        this.packingMethod = packingMethod;
    }

    public String getTransportConsiderations() {
        return transportConsiderations;
    }

    public void setTransportConsiderations(String transportConsiderations) {
        this.transportConsiderations = transportConsiderations;
    }

    public String getRegulatoryInformation() {
        return regulatoryInformation;
    }

    public void setRegulatoryInformation(String regulatoryInformation) {
        this.regulatoryInformation = regulatoryInformation;
    }
}
