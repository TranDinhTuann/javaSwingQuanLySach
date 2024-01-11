package it6020002.objects;

public class PgObject {
	private short pg_id;
	private String pg_name;
	private byte pg_ps_id;
	private int pg_manager_id;
	private String pg_notes;
	private boolean pg_delete; 
	private String pg_deleted_date;
	private String pg_deleted_author;
	private String pg_modified_date;
	private String pg_created_date;
	private boolean pg_enable; 
	private String pg_name_en;
	private int pg_created_author_id;
	private byte pg_language;

	public PgObject() {

	}

	public short getPg_id() {
		return pg_id;
	}

	public String getPg_name() {
		return pg_name;
	}

	public byte getPg_ps_id() {
		return pg_ps_id;
	}

	public int getPg_manager_id() {
		return pg_manager_id;
	}

	public String getPg_notes() {
		return pg_notes;
	}

	public boolean isPg_delete() {
		return pg_delete;
	}

	public String getPg_deleted_date() {
		return pg_deleted_date;
	}

	public String getPg_deleted_author() {
		return pg_deleted_author;
	}

	public String getPg_modified_date() {
		return pg_modified_date;
	}

	public String getPg_created_date() {
		return pg_created_date;
	}

	public boolean isPg_enable() {
		return pg_enable;
	}

	public String getPg_name_en() {
		return pg_name_en;
	}

	public int getPg_created_author_id() {
		return pg_created_author_id;
	}

	public byte getPg_language() {
		return pg_language;
	}

	public void setPg_id(short pg_id) {
		this.pg_id = pg_id;
	}

	public void setPg_name(String pg_name) {
		this.pg_name = pg_name;
	}

	public void setPg_ps_id(byte pg_ps_id) {
		this.pg_ps_id = pg_ps_id;
	}

	public void setPg_manager_id(int pg_manager_id) {
		this.pg_manager_id = pg_manager_id;
	}

	public void setPg_notes(String pg_notes) {
		this.pg_notes = pg_notes;
	}

	public void setPg_delete(boolean pg_delete) {
		this.pg_delete = pg_delete;
	}

	public void setPg_deleted_date(String pg_deleted_date) {
		this.pg_deleted_date = pg_deleted_date;
	}

	public void setPg_deleted_author(String pg_deleted_author) {
		this.pg_deleted_author = pg_deleted_author;
	}

	public void setPg_modified_date(String pg_modified_date) {
		this.pg_modified_date = pg_modified_date;
	}

	public void setPg_created_date(String pg_created_date) {
		this.pg_created_date = pg_created_date;
	}

	public void setPg_enable(boolean pg_enable) {
		this.pg_enable = pg_enable;
	}

	public void setPg_name_en(String pg_name_en) {
		this.pg_name_en = pg_name_en;
	}

	public void setPg_created_author_id(int pg_created_author_id) {
		this.pg_created_author_id = pg_created_author_id;
	}

	public void setPg_language(byte pg_language) {
		this.pg_language = pg_language;
	}

	
}
