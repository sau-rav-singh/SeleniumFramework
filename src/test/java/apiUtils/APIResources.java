package apiUtils;

public enum APIResources {

	AddComment("rest/api/3/issue/{id}/comment");

	private String resource;

	APIResources(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}
}
