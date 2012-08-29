package monsterbreeder.monster;

public enum PartType 
{
	BODY,
	HEAD,
	ARM,
	LEG,
	WING,
	SPECIAL;

	public static PartType get( String name )
	{
		PartType[] vals = values();
		
		for( PartType pt : vals )
		{
			if( pt.name().equalsIgnoreCase( name ) )
			{
				return pt;
			}
		}
		return null;
	}
}
