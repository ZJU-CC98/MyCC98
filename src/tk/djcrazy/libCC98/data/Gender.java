package tk.djcrazy.libCC98.data;

public enum Gender {
	MALE {
		@Override
		public String getName() {
			return "Male";
		}
	},
	FEMALE {
		@Override
		public String getName() {
			return "FeMale";
		}
	};
	
	public abstract String getName();
}
