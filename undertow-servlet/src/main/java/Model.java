import org.sql2o.Sql2o;

public class Model {
	private static Sql2o sql2o;

	public static Sql2o getSql2o() {
		return sql2o;
	}

	public static void setSql2o(Sql2o sql2o) {
		Model.sql2o = sql2o;
	}
}
