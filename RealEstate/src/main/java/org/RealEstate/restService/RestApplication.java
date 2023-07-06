package org.RealEstate.restService;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RestApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(GovernorateMangment.class);
		classes.add(PostMangment.class);
		classes.add(DistrictMangment.class);

		classes.add(VillageMangment.class);

		classes.add(WaterResourcesMangment.class);
		classes.add(UserMangment.class);
		classes.add(GeneralConfigurationMangment.class);

		classes.add(AdsMangment.class);

		return classes;
	}
}
