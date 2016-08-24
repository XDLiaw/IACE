package iace.service.enterpriseNeed;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.util.PagedList;
import iace.dao.enterpriseNeed.IEnterpriseInfoDao;
import iace.dao.option.IOptionDao;
import iace.entity.enterpriseNeed.EnterpriseAcademiaCoop;
import iace.entity.enterpriseNeed.EnterpriseInfo;
import iace.entity.enterpriseNeed.EnterpriseNeedSearchModel;
import iace.entity.enterpriseNeed.EnterpriseRequireTech;
import iace.entity.enterpriseNeed.EnterpriseSituation;
import iace.entity.option.OptionCompanyLocation;
import iace.entity.option.OptionCooperateMode;
import iace.entity.option.OptionHadTecSrc;
import iace.entity.option.OptionDomain;
import iace.service.BaseIaceService;

public class EnterpriseInfoService extends BaseIaceService<EnterpriseInfo> {

	private IEnterpriseInfoDao enterpriseInfoDao;
	private IOptionDao<OptionDomain> optIndustryDao;
	private IOptionDao<OptionCompanyLocation> optCompanyLocationDao;
	private IOptionDao<OptionHadTecSrc> optHadTecSrcDao;
	private IOptionDao<OptionCooperateMode> optCooperateModeDao;
	
	public EnterpriseInfoService(IEnterpriseInfoDao dao, 
			IOptionDao<OptionDomain> optIndustryDao,
			IOptionDao<OptionCompanyLocation> optCompanyLocationDao, 
			IOptionDao<OptionHadTecSrc> optHadTecSrcDao, 
			IOptionDao<OptionCooperateMode> optCooperateModeDao) {
		super(dao);
		this.enterpriseInfoDao = dao;
		this.optIndustryDao = optIndustryDao;
		this.optCompanyLocationDao = optCompanyLocationDao;
		this.optHadTecSrcDao = optHadTecSrcDao;
		this.optCooperateModeDao = optCooperateModeDao;
	}
		
	public PagedList<EnterpriseInfo> searchBy(EnterpriseNeedSearchModel searchCondition) {
		return this.enterpriseInfoDao.searchBy(searchCondition);
	}
	
	@Override
	public void create(EnterpriseInfo entity) throws IOException, SQLException {
		setOptionIndustryList(entity);
		setOptionCompanyLocation(entity);		
		
		EnterpriseRequireTech requireTech = entity.getEnterpriseRequireTech();
		if (requireTech == null) {
			requireTech = new EnterpriseRequireTech();
			entity.setEnterpriseRequireTech(requireTech);
		}
		setPhase1(requireTech);
		requireTech.create();
		requireTech.setEnterpriseInfo(entity);

		EnterpriseSituation situation = entity.getEnterpriseSituation();
		if (situation == null) {
			situation = new EnterpriseSituation();
			entity.setEnterpriseSituation(situation);
		}
		setHadTecSrc(situation);
		setCoopMode(situation);
		situation.create();
		situation.setEnterpriseInfo(entity);
		
		EnterpriseAcademiaCoop academiaCoop = entity.getEnterpriseAcademiaCoop();
		if (academiaCoop == null) {
			academiaCoop = new EnterpriseAcademiaCoop();
			entity.setEnterpriseAcademiaCoop(academiaCoop);
		}
		academiaCoop.create();
		academiaCoop.setEnterpriseInfo(entity);
		
		super.create(entity);
	}
	
	@Override
	public void update(EnterpriseInfo entity) throws IOException, SQLException {
		setOptionIndustryList(entity);
		setOptionCompanyLocation(entity);		

		EnterpriseRequireTech requireTech = entity.getEnterpriseRequireTech();
		setPhase1(requireTech);
		requireTech.update();
		requireTech.setEnterpriseInfo(entity);

		EnterpriseSituation situation = entity.getEnterpriseSituation();
		setHadTecSrc(situation);
		setCoopMode(situation);
		situation.update();
		situation.setEnterpriseInfo(entity);
		
		EnterpriseAcademiaCoop academiaCoop = entity.getEnterpriseAcademiaCoop();
		academiaCoop.update();
		academiaCoop.setEnterpriseInfo(entity);
		
		super.update(entity);
	}
	
	private void setOptionCompanyLocation(EnterpriseInfo entity) {
		if (entity.getOptionCompanyLocation() != null) {
			long optId = entity.getOptionCompanyLocation().getId();
			OptionCompanyLocation opt = this.optCompanyLocationDao.get(optId);
			entity.setOptionCompanyLocation(opt);
		}
	}
	
	private void setOptionIndustryList(EnterpriseInfo entity) {
		if (entity.getOptionDomainList() != null) {
			List<OptionDomain> newList = new ArrayList<OptionDomain>();
			for (OptionDomain opt : entity.getOptionDomainList()) {
				newList.add(this.optIndustryDao.get(opt.getId()));
			}
			entity.setOptionDomainList(newList);
		}
	}
	
	private void setPhase1(EnterpriseRequireTech entity) {
		if (entity.getPhase1() != null) {
			long optId = entity.getPhase1().getId();
			OptionDomain opt = this.optIndustryDao.get(optId);
			entity.setPhase1(opt);
		}
	}
	
	private void setHadTecSrc(EnterpriseSituation entity) {
		if (entity.getOptionHadTecSrc() != null) {
			long optId = entity.getOptionHadTecSrc().getId();
			OptionHadTecSrc opt = this.optHadTecSrcDao.get(optId);
			entity.setOptionHadTecSrc(opt);
		}
	}
	
	private void setCoopMode(EnterpriseSituation entity) {
		if (entity.getOptionCooperateMode() != null) {
			long optId = entity.getOptionCooperateMode().getId();
			OptionCooperateMode opt = this.optCooperateModeDao.get(optId);
			entity.setOptionCooperateMode(opt);
		}
	}


}
