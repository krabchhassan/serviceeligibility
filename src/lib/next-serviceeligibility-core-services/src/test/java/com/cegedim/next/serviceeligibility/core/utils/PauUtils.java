package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PauUtils {

  public static GenericRightDto getContratDeBase() {
    GenericRightDto c = new GenericRightDto();
    c.setContext("TP_ONLINE");
    c.setInsurerId("0000401166");
    c.setOcCode("BALOO");
    c.setNumber("1394020001");
    c.setExternalNumber("1394020001");
    c.setSubscriberId("1394020001");
    c.setSubscriberFullId("1394020001-COMPLET");
    c.setSubscriptionDate("2021-01-01");
    c.setTerminationDate(null);
    c.setBusinessContributor("Courtier & Co");

    List<Period> prcops = new ArrayList<>();
    Period prcop = new Period();
    prcop.setStart("2021-01-01");
    prcop.setEnd("2021-12-31");
    prcops.add(prcop);
    c.setResponsibleContractOpenPeriods(prcops);

    c.setCmuContractOpenPeriods(new ArrayList<>());

    c.setSecondaryCriterion("Employés");
    c.setDetailedSecondaryCriterion("GEO-FRA-OCC");
    c.setIsIndividualContract(true);
    c.setOperator("IGestion");

    CollectiveContractV5 cc = new CollectiveContractV5();
    cc.setNumber("03094493");
    cc.setExternalNumber("DKMMMS09049304930");
    c.setCollectiveContract(cc);

    c.setQualification("B");
    c.setPrioritizationOrder("1");

    c.setSuspensionPeriods(new ArrayList<>());

    c.setInsured(getAssure());
    return c;
  }

  private static Insured getAssure() {
    Insured i = new Insured();
    i.setIsSubscriber(false);
    i.setAdministrativeRank("2");

    Identity id = new Identity();
    Nir nir = new Nir();
    nir.setCode("1041059321456");
    nir.setKey("69");
    id.setNir(nir);
    AffiliationRO aff = new AffiliationRO();
    Nir nirAff = new Nir();
    nirAff.setCode("1791059632524");
    nirAff.setKey("91");
    aff.setNir(nirAff);
    AttachementRO att = new AttachementRO();
    att.setRegimeCode("01");
    att.setHealthInsuranceCompanyCode("624");
    att.setCenterCode("0156");
    aff.setAttachementRO(att);
    Period paff = new Period();
    paff.setStart("2022-02-14");
    paff.setEnd(null);
    aff.setPeriod(paff);

    AffiliationRO aff2 = new AffiliationRO();
    Nir nirAff2 = new Nir();
    nirAff2.setCode("1791059632524");
    nirAff2.setKey("91");
    aff2.setNir(nirAff2);
    AttachementRO att2 = new AttachementRO();
    att2.setRegimeCode("01");
    att2.setHealthInsuranceCompanyCode("595");
    att2.setCenterCode("0123");
    aff2.setAttachementRO(att2);
    Period paff2 = new Period();
    paff2.setStart("2020-01-01");
    paff2.setEnd("2022-02-13");
    aff2.setPeriod(paff2);
    List<AffiliationRO> affs = new ArrayList<>();
    affs.add(aff);
    affs.add(aff2);
    id.setAffiliationsRO(affs);

    id.setBirthDate("20041026");
    id.setBirthRank("1");
    id.setPersonNumber("0000401166-1394020001-002");
    id.setPersonExternalRef("EXT-1394020001-002");

    i.setIdentity(id);

    InsuredData data = new InsuredData();
    Name nom = new Name();
    nom.setLastName("PEHAHUT");
    nom.setCommonName("VAITROUAT");
    nom.setFirstName("FARID");
    nom.setCivility("Mr");
    data.setName(nom);

    Address address = new Address();
    address.setLine1("MR PEHAHUT VAITROUAT KARIM");
    address.setLine4("13 RUE DES TULIPES");
    address.setLine6("59250 HALLUIN");
    address.setPostcode("59250");
    data.setAddress(address);

    Contact contact = new Contact();
    contact.setLandline("0321992364");
    contact.setMobile("0735427677");
    contact.setEmail(null);
    data.setContact(contact);

    PaymentRecipient pr = new PaymentRecipient();
    pr.setPaymentRecipientId("DP-1394020001");
    pr.setBeyondPaymentRecipientId(null);
    NameCorporate nc = new NameCorporate();
    nc.setLastName("PEHAHUT");
    nc.setCommonName("VAITROUAT");
    nc.setFirstName("KARIM");
    nc.setCivility("Mr");
    nc.setCorporateName(null);
    pr.setName(nc);
    Address addCorp = new Address();
    addCorp.setLine1("MR PEHAHUT VAITROUAT KARIM");
    addCorp.setLine4("13 RUE DES TULIPES");
    addCorp.setLine6("59250 HALLUIN");
    addCorp.setPostcode("59250");
    pr.setAddress(addCorp);

    Rib rib = new Rib();
    rib.setBic("MCCFFRP1");
    rib.setIban("FR3330002005500000157841Z25");
    pr.setRib(rib);

    BenefitPaymentMode bpm = new BenefitPaymentMode();
    bpm.setCode("VIR");
    bpm.setLabel("Virement");
    bpm.setCurrencyCode("EUR");
    pr.setBenefitPaymentMode(bpm);

    Period ppr = new Period();
    ppr.setStart("2021-01-01");
    ppr.setEnd(null);
    pr.setPeriod(ppr);
    List<PaymentRecipient> prs = new ArrayList<>();
    prs.add(pr);
    data.setPaymentRecipients(prs);

    BenefitStatementRecipient bsr = new BenefitStatementRecipient();
    bsr.setBenefitStatementRecipientId("DRP-1394020001");
    bsr.setBeyondBenefitStatementRecipientId(null);
    NameCorporate nbsr = new NameCorporate();
    nbsr.setLastName("PEHAHUT");
    nbsr.setCommonName("VAITROUAT");
    nbsr.setFirstName("KARIM");
    nbsr.setCivility("Mr");
    nbsr.setCorporateName(null);
    bsr.setName(nbsr);
    Address addbsr = new Address();
    addbsr.setLine1("MR PEHAHUT VAITROUAT KARIM");
    addbsr.setLine4("13 RUE DES TULIPES");
    addbsr.setLine6("59250 HALLUIN");
    addbsr.setPostcode("59250");
    bsr.setAddress(addbsr);
    Period pbsr = new Period();
    pbsr.setStart("2021-01-01");
    pbsr.setEnd(null);
    bsr.setPeriod(pbsr);

    List<BenefitStatementRecipient> bsrs = new ArrayList<>();
    bsrs.add(bsr);
    data.setBenefitStatementRecipients(bsrs);

    i.setData(data);

    i.setHealthMutualSubscriptionDate("2021-01-01");
    i.setIndividualSubscriptionStartDate("2021-01-01");
    i.setIndividualSubscriptionNumber("120320020");
    i.setCancellationDate(null);

    DigitRelation digit = new DigitRelation();
    Dematerialization demat = new Dematerialization();
    demat.setIsDematerialized(true);
    demat.setEmail(null);
    demat.setMobile("0735427677");
    digit.setDematerialization(demat);
    RemoteTransmission rt = new RemoteTransmission();
    Period prt = new Period();
    prt.setStart("2021-01-01");
    prt.setEnd(null);
    rt.setPeriod(prt);
    rt.setIsRemotelyTransmitted(true);
    List<RemoteTransmission> rts = new ArrayList<>();
    rts.add(rt);
    digit.setRemoteTransmissions(rts);
    i.setDigitRelation(digit);

    Period papop1 = new Period();
    papop1.setStart("2021-01-01");
    papop1.setEnd("2021-12-31");
    Period papop2 = new Period();
    papop2.setStart("2020-01-01");
    papop2.setEnd("2020-12-31");
    List<Period> papops = new ArrayList<>();
    papops.add(papop1);
    papops.add(papop2);
    i.setAttendingPhysicianOpenedPeriods(papops);

    SpecialPlan sp = new SpecialPlan();
    sp.setCode("AM");
    Period psp = new Period();
    psp.setStart("2021-01-01");
    psp.setEnd("2021-12-31");
    sp.setPeriod(psp);
    List<SpecialPlan> sps = new ArrayList<>();
    sps.add(sp);
    i.setSpecialPlans(sps);

    SpecialStatus ss = new SpecialStatus();
    ss.setCode("ALD");
    Period pss = new Period();
    pss.setStart("2021-01-01");
    pss.setEnd("2021-12-31");
    ss.setPeriod(pss);
    List<SpecialStatus> sss = new ArrayList<>();
    sss.add(ss);
    i.setSpecialStatuses(sss);

    Quality q = new Quality();
    q.setCode("A");
    q.setLabel("Enfant");
    i.setQuality(q);

    i.setRights(getDroits());

    return i;
  }

  private static List<Right> getDroits() {
    List<Right> droits = new ArrayList<>();

    Right droit = new Right();
    droit.setCode("BAL_BASE");
    droit.setInsurerCode("ASS_BALOO");
    droit.setType("BASE");
    droit.setPrioritizationOrder("1");
    droit.setGuaranteeAgeDate("2022-01-01");

    Product product = new Product();
    product.setIssuingCompanyCode("BALOO");
    product.setIssuingCompanyName("Baloo");
    product.setOfferCode("BLUE_TP_01");
    product.setProductCode("SANTE_100");
    BenefitType benefitType = new BenefitType();
    benefitType.setBenefitType("OPTIQUE");
    Domain domain = new Domain("OPTI", "");
    benefitType.setDomains(List.of(domain));
    product.setBenefitsType(new HashSet<>(List.of(benefitType)));
    Period pprod = new Period();
    pprod.setStart("2022-01-10");
    pprod.setEnd("2022-01-10");
    product.setPeriod(pprod);
    List<Product> products = new ArrayList<>();
    products.add(product);
    droit.setProducts(products);

    droits.add(droit);
    return droits;
  }
}
