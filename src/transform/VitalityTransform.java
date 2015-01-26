package transform;

import java.util.ArrayList;
import java.util.List;

import entity.Commit;
import entity.Project;
import entity.User;
import usefuldata.Vitality;

public class VitalityTransform {
	public List<Vitality> transform(User user,Project project){
		List<Vitality> vitalities = new ArrayList<Vitality>();
		List<Commit> commits = user.getCommits();
		for(Commit commit:commits){
			if(commit.getProject().equals(project)){
				Vitality v = new Vitality();
				String s = v.dateTrim(commit.getCommitDate());
				v.setDate(s);
				v.setVitality(commit.getAdditionsCount());
				vitalities.add(v);
			}
		}
		
		
		return vitalities;
	}
	
}
