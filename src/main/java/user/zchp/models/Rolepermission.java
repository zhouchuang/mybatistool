package user.zchp.models;import lombok.Data;import user.zchp.utils.BaseEntity;//表名称 rolepermission@Datapublic class Rolepermission extends BaseEntity {	private static final long serialVersionUID = 1L;	public Rolepermission() {		super();	}	    private String roleId;    private String permissionId;}