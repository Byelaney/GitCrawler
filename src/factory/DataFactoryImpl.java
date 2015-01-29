package factory;



import java.util.ArrayList;

import analysis.CodeLinesCount;
import analysis.CodeLinesCountImpl;
import analysis.DevelopDigram;
import analysis.DevelopDigramImpl;
import analysis.PackageDependency;
import analysis.PackageDependencyImpl;
import analysis.RelationImpl;
import analysis.Relation;

public class DataFactoryImpl implements DataFactory{
	@Override
	public ArrayList<String> getPakageDependency(ArrayList<String> files,
			ArrayList<String> languages) {
		PackageDependency pd=new PackageDependencyImpl();
		return pd.getPakageDependency(files,languages);
	}

	@Override
	public int getCodeLines(String file,ArrayList<String> languages) {
		CodeLinesCount cl=new CodeLinesCountImpl();
		return cl.getCodeLines(file,languages);
	}

	@Override
	public String getDevelopDigramByVersion(ArrayList<String> filenames) {
		DevelopDigram dd=new DevelopDigramImpl();
		return dd.getDevelopDigramByVersion(filenames);
	}

	@Override
	public String getRelationsDestination(ArrayList<String> developers,
			String projectName, String releaseName) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

//	@Override
//	public String getRelationsDestination(ArrayList<String> developers,String projectName, String releaseName) {
//		// TODO Auto-generated method stub
//		Relation relations=new RelationImpl();
//		return relations.getRelations(developers, projectName, releaseName);
//	}
}
