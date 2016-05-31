package iace.service;

import java.util.List;

import core.service.BaseService;
import iace.dao.option.IOptionDao;
import iace.entity.option.BaseOption;

public abstract class BaseOptionService<OptionEntity extends BaseOption> extends BaseService<OptionEntity, Long> {
	protected IOptionDao<OptionEntity> dao;

	protected BaseOptionService(IOptionDao<OptionEntity> dao) {
		this.dao = dao;
	}
	
	public List<OptionEntity> listAll() {
		return dao.listAll();
	}
	
	public List<OptionEntity> listNotIn(List<String> codes) {
		return dao.listNotIn(codes);
	}
	
	@Override
	public OptionEntity get(Long id) {
		return dao.get(id);
	}

	@Override
	public void create(OptionEntity entity) {
		dao.create(entity);
	}

	@Override
	public void update(OptionEntity entity) {
		dao.update(entity);
	}

	@Override
	public void delete(OptionEntity entity) {
		if (hasBeenUsed(entity)) {
			String msg = "Can't be delete because it had been used!";
			throw new IllegalArgumentException(msg);
		}
		dao.delete(entity);		
	}
	
	public void delete(Long id) {
		if (hasBeenUsed(id)) {
			String msg = "Can't be delete because it had been used!";
			throw new IllegalArgumentException(msg);
		}
		dao.delete(id);
	}
	
	public boolean isCodeExist(String code) {
		return dao.isCodeExist(code);
	}
	
	public boolean hasBeenUsed(OptionEntity entity) {
		return dao.hasBeenUsed(entity);
	}
	
	public boolean hasBeenUsed(Long id) {
		return dao.hasBeenUsed(id);
	}
}
