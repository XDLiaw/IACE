package iace.dao.talentedPeople;

import java.util.List;

import core.util.PagedList;
import iace.dao.IBaseIaceDao;
import iace.entity.sys.SysUser;
import iace.entity.talentedPeople.TalentedPeople;
import iace.entity.talentedPeople.TalentedPeopleSearchModel;

public interface ITalentedPeopleDao extends IBaseIaceDao<TalentedPeople> {
	
	public PagedList<TalentedPeople> searchBy(TalentedPeopleSearchModel arg);
	
	public PagedList<TalentedPeople> searchBy(TalentedPeopleSearchModel arg, boolean allEager);
	
	public List<TalentedPeople> listAll(TalentedPeopleSearchModel arg);
	
	public List<TalentedPeople> listAllWithSysUser();
	
	public List<TalentedPeople> listNotAgreePDPLYet();
	
	public TalentedPeople get(SysUser user);

	public long queryTotalRecordsCount(TalentedPeopleSearchModel arg);
	
	public List<Long> getAllIdUnderDomain(Long mainDomainId);
}
