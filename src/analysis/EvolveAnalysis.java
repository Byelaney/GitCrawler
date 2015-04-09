package analysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import usefuldata.Comment;
import usefuldata.CommitDate;
import usefuldata.Link;
import usefuldata.Movement;
import usefuldata.Node;
import usefuldata.Radar;
import usefuldata.Version;
import usefuldata.VersionDate;
import net.sf.json.JSONArray;

public class EvolveAnalysis {
	private DataHelper dh;
	private ArrayList<String> developers;
	private String projectName;
	// private ArrayList<String> releases;
	private List<Movement> movements;
	private ArrayList<Integer> developerSizeInVersion;
	private ArrayList<CommitDate> allCommits;
	private ArrayList<VersionDate> versionDates;
	private int[] sizeRank = { 180, 130, 100, 75, 55, 40, 25, 15, 10, 5 };

	public EvolveAnalysis(String projectName) {
		dh = new DataHelperImpl();
		this.projectName = projectName;

		versionDates = dh.getVersions(projectName);
		versionDates = orderVersions(versionDates);
		
		allCommits = dh.getCommits(projectName);
		developerSizeInVersion = new ArrayList<Integer>();

		developers = dh.getAllDeveloperNames(projectName);
		// releases = dh.getReleaseNames(projectName);
		movements = new ArrayList<Movement>();

	}

	private String getNodesJson(String releaseName) {
		List<Node> nodes = new ArrayList<Node>();
		ArrayList<String> nodeNames = new ArrayList<String>();
		for (Movement mm : movements) {
			if (mm.getMove().equals("join")) {
				String dname = mm.getName();
				if (!nodeNames.contains(dname))
					nodeNames.add(dname);
			}
		}

		int i = 0;
//		int maxDsize = 0;
//		for (; i < nodeNames.size(); i++) {
//			String developerName = nodeNames.get(i);
//			if (Math.log(dh.getSize(developerName, projectName, releaseName)) > maxDsize)
//				maxDsize = dh.getSize(developerName, projectName, releaseName);
//		}
//		i = 0;
		for (; i < nodeNames.size(); i++) {
			String developerName = nodeNames.get(i);
			int dsize = 0;
			 int index=getVersionDateNum(releaseName);
			 for(VersionDate vd:versionDates)
			 {  
				 if(vd.getOrder()<=index)
				 {
					 dsize+=dh.getSize(developerName, projectName, vd.getVersion());
				 }
			 }
			
//			if (Math.log(dh.getSize(developerName, projectName, releaseName)) < 1)
//				dsize = (int) Math.log(dh.getSize(developerName, projectName,
//						releaseName)) + 1;
//			else
//				dsize = (int) Math.log(dh.getSize(developerName, projectName,
//						releaseName));
//			dsize = dsize * 100 / maxDsize;

			Node node = new Node(developerName, dsize);

			nodes.add(node);
		}
		sort(nodes,0, nodes.size() - 1);

		i=0;
		for (; i < nodes.size(); i++) {
			Node node = null;
			node = nodes.get(i);
			if (i < 10)
				node.setSize(sizeRank[i]);
			else
				node.setSize(sizeRank[9] + 30);

		}// 将size调整为比较好显示的数据
		JSONArray json = JSONArray.fromObject(nodes);
		String jsonStr = json.toString();
		return jsonStr;
	}
	
	
	private List<Node> sort(List<Node> ns,int low, int high) {
		List<Node> nodes=new ArrayList<Node>();
		nodes=ns;
		int l = low;
		int h = high;
		int povit = nodes.get(low).getSize();

		while (l < h) {
			while (l < h && nodes.get(h).getSize() <= povit)
				h--;
			if (l < h) {
				Node temp = nodes.get(h);
				nodes.set(h, nodes.get(l));
				nodes.set(l, temp);
				l++;

			}
			while (l < h && nodes.get(l).getSize() >= povit)
				l++;
			if (l < h) {
				Node temp = nodes.get(h);
				nodes.set(h, nodes.get(l));
				nodes.set(l, temp);
				h--;
			}

		}
	

		if (l > low)
			return sort(nodes,low, h - 1);
		if (h < high)
			return sort(nodes,l + 1, high);
		return nodes;
	}


	private boolean isLinked(ArrayList<String> filenames,
			ArrayList<String> filenamesToCompare) {
		boolean result = false;

		for (String file : filenames) {
			int l = file.split("/").length;
			String filename = file.split("/")[l - 1];
			String filePath = file.substring(0,
					file.length() - filename.length());

			for (String fileToComepare : filenamesToCompare) {
				int l2 = fileToComepare.split("/").length;
				String filename2 = fileToComepare.split("/")[l2 - 1];
				String filePath2 = fileToComepare.substring(0,
						fileToComepare.length() - filename2.length());

				// System.out.println("filePath:"+filePath);
				// System.out.println("filePath2:"+filePath2);
				if (filePath.equals(filePath2)) {
					result = true;
					break;
				}
			}
			if (result)
				break;
		}

		return result;
	}

	private String getLinksJson(String releaseName) {
		VersionDate vd = new VersionDate();
		vd = getVersionDate(releaseName);// get release date

		ArrayList<String> nodeNames = new ArrayList<String>();
		for (Movement mm : movements) {
			if (mm.getMove().equals("join")) {
				String dname = mm.getName();
				if (!nodeNames.contains(dname))
					nodeNames.add(dname);
			}
		}
		List<Link> links = new ArrayList<Link>();
		int i = 0;
		for (; i < nodeNames.size(); i++) {
			String developerName1 = nodeNames.get(i);
			ArrayList<String> files1 = new ArrayList<String>();
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
			String dtStr = sdf.format(now);// get instant time

			files1 = dh.getFiles(projectName, developerName1, vd.getDate(),
					dtStr);
			int j = 0;
			for (; j < nodeNames.size(); j++) {
				if (j != i) {
					String developerName2 = nodeNames.get(j);
					ArrayList<String> files2 = new ArrayList<String>();

					files2 = dh.getFiles(projectName, developerName2,
							vd.getDate(), dtStr);
					Link link = new Link(i, j);
					boolean linked = false;
					linked = isLinked(files1, files2);
					if (linked) {
						// System.out.println("Linked!!");
						if (links.size() == 0) {
							links.add(link);
						} else {

							boolean add = true;
							for (int p = 0; p < links.size(); p++) {
								Link l = links.get(p);
								int s = l.getSource();
								int t = l.getTarget();

								if (s == link.getTarget()
										&& t == link.getSource())
									add = false;
								if (t == link.getTarget()
										&& s == link.getSource())
									add = false;
							}
							if (add) {
								links.add(link);
							}
						}
					}
				}
			}
		}
		JSONArray json = JSONArray.fromObject(links);
		String jsonStr = json.toString();
		return jsonStr;

	}

	/**
	 * 从前到后的发布顺序排序版本
	 * 
	 * @param v
	 * @return
	 */
	private ArrayList<VersionDate> orderVersions(ArrayList<VersionDate> v) {
		ArrayList<VersionDate> versions = new ArrayList<VersionDate>();

		versions = v;
		int i = versions.size() - 1;
		for (; i > 0; --i) {
			int j = 0;
			for (; j < i; ++j) {

				if (versions.get(i).getOrder() < versions.get(j).getOrder()) {
					// System.out.println("ij:" + i + "," + j);
					VersionDate temp = versions.get(j);
					versions.set(j, versions.get(i));
					versions.set(i, temp);
					break;

				}
			}

		}
		
//		System.out.println(versions.size());
//		for(VersionDate vd:versions)
//		{
//			System.out.println(vd.getVersion()+":"+vd.getOrder());
//			System.out.println(vd.getVersion()+":"+vd.getDate());
//		}

		return versions;

	}

	/**
	 * 两次日期比较，是否前一个大于等于（近于）后一个
	 * 
	 * @param date
	 * @param dateToCompare
	 * @return
	 */
	private boolean DateIsGreater(String date, String dateToCompare) {
		int date1 = Integer.parseInt(date.replace("-", ""));
		int date2 = Integer.parseInt(dateToCompare.replace("-", ""));

		return date1 >= date2;
	}

	/*
	*//**
	 * 得到一个发布版本中所有的commit
	 * 
	 * @param commits
	 * @param release
	 * @return
	 */
	/*
	 * private ArrayList<CommitDate> CommitsInOneRelease( ArrayList<CommitDate>
	 * commits, String release) { ArrayList<CommitDate> commitsInOneRelease =
	 * new ArrayList<CommitDate>(); ArrayList<CommitDate> allCommits = new
	 * ArrayList<CommitDate>(); allCommits = commits; int i = 0; for (; i <
	 * allCommits.size(); i++) { if (DateIsGreater(release,
	 * allCommits.get(i).getDate())) { commitsInOneRelease.add(commits.get(i));
	 * }
	 * 
	 * } return commitsInOneRelease; }
	 */
	//
	/**
	 * 获得某个用户最近一次的迁移情况
	 * 
	 * @param developerName
	 * @return
	 */
	private Movement getLatestMoveOfOne(String developerName) {
		ArrayList<Movement> mms = new ArrayList<Movement>();

		for (Movement mm : movements) {
			if (mm.getName().equals(developerName)) {
				mms.add(mm);
			}
		}

		int i = mms.size() - 1;
		for (; i > 0; --i) {
			Movement mm1 = new Movement();
			mm1 = mms.get(i);
			int j = 0;
			for (; j < i; ++j) {
				Movement mm2 = mms.get(j);
				if (DateIsGreater(mm1.getDate(), mm2.getDate())) {
					Movement temp = mm2;
					mms.set(j, mm1);
					mms.set(i, temp);
				}
			}
		}

		if (mms.size() > 0) {

			return mms.get(0);
		} else
			return null;

	}

	/**
	 * 某开发者之前是否加入过项目
	 * 
	 * @param developerName
	 * @return
	 */
	private boolean isJoined(String developerName) {
		boolean result = false;
		for (Movement mm : movements) {
			if (mm.getName().equals(developerName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 获得从某个版本到今的所有VersionDate
	 * 
	 * @param release
	 * @return
	 */
	private ArrayList<VersionDate> getVersionDatesTillNow(String release) {
		ArrayList<VersionDate> versions = new ArrayList<VersionDate>();
		VersionDate versiondate = new VersionDate();
		versiondate = getVersionDate(release);
		for (VersionDate vd : versionDates) {

			if (DateIsGreater(vd.getDate(), versiondate.getDate())) {
				versions.add(vd);

			}

		}

		return versions;
	}

	/**
	 * 跟据版本号取得VersionDate实体
	 * 
	 * @param release
	 * @return VersionDate
	 */
	private VersionDate getVersionDate(String release) {
		VersionDate versiondate = new VersionDate();
		for (VersionDate vd : versionDates) {
			if (vd.getVersion().equals(release)) {
				versiondate = vd;
				break;
			}
		}

		return versiondate;
	}

	/**
	 * 跟据版本号取得VersionDate序号,是所有版本中的第几个版本
	 * 
	 * @param release
	 * @return VersionDate
	 */
	private int getVersionDateNum(String release) {
		int result = -1;
		int i = 0;
		for (; i < versionDates.size(); i++) {
			if (versionDates.get(i).getVersion().equals(release)) {
				result = i;
				break;
			}
		}

		return result;
	}

	/**
	 * 跟据版本号取得上一个VersionDate实体
	 * 
	 * @param release
	 * @return VersionDate
	 */

	private VersionDate getPreVersionDate(String release) {
		VersionDate versiondate = new VersionDate();

		for (VersionDate vd : versionDates) {
			if (vd.getVersion().equals(release)) {
				versiondate = vd;
				break;
			}
		}

		int i = 0;

		for (; i < versionDates.size(); i++) {
			if (versiondate.getVersion().equals(
					versionDates.get(i).getVersion())) {
				if (i != 0)
					versiondate = versionDates.get(i - 1);
				else
					versiondate = null;
			}

		}
		return versiondate;
	}

	/**
	 * 得到从某个版本发布时间到现在的所有CommitDate
	 * 
	 * @param release
	 * @return
	 */
	private ArrayList<CommitDate> getCommitFromVersionToNow(String release) {
		ArrayList<CommitDate> commits = new ArrayList<CommitDate>();

		VersionDate versiondate = new VersionDate();
		versiondate = getVersionDate(release);

		for (CommitDate cd : allCommits) {
			if (DateIsGreater(cd.getDate(), versiondate.getDate())) {
				commits.add(cd);
			}
		}

		return commits;

	}

	private String getMovementsJson(String release) {
		// 运行此方法 还会取得在所请求版本号的前一个版本发布时间往后所有的movement，保存在全局变量里。
		
		ArrayList<CommitDate> commits = new ArrayList<CommitDate>();
		ArrayList<VersionDate> versions = new ArrayList<VersionDate>();
		versions = getVersionDatesTillNow(release);// 得到包括请求在内，往后的所有开发版本号
		versions = orderVersions(versions);// 对其发布顺序从前到后排序
		int i = 0;
		for (; i <= versions.size(); i++) {
			allCommits = dh.getCommits(projectName);

			if (i == versions.size()) {// 算最后一个版本之后的commits
				VersionDate versiondate = new VersionDate();
				versiondate = versions.get(i - 1);
				commits = getCommitFromVersionToNow(versiondate.getVersion());

			} else if (getVersionDateNum(versions.get(i).getVersion()) == 0) {// 如果请求的版本号是第一个版本，选返回所有commits再减去第一个版本发布时间后的所有commits
				commits = allCommits;
				VersionDate versiondate = new VersionDate();
				versiondate = versions.get(i);
				ArrayList<CommitDate> commitsToRemove = new ArrayList<CommitDate>();
				commitsToRemove = getCommitFromVersionToNow(versiondate
						.getVersion());// 第一个版本发布时间后的所有commits
				commits.removeAll(commitsToRemove);

			} else {// 否则返回在之前的一个版本发布时间往后的所有commit,再减去所请求版本发布时间后的所有commits

				VersionDate versiondate = new VersionDate();
				int versionNum = getVersionDateNum(versions.get(i).getVersion());
				versiondate = versionDates.get(versionNum - 1);
				commits = getCommitFromVersionToNow(versiondate.getVersion());
				versiondate = new VersionDate();
				versiondate = versions.get(i);
				ArrayList<CommitDate> commitsToRemove = new ArrayList<CommitDate>();
				commitsToRemove = getCommitFromVersionToNow(versiondate
						.getVersion());
				commits.removeAll(commitsToRemove);

			}

			developers = dh.getAllDeveloperNames(projectName);
			ArrayList<String> developersInVersion = new ArrayList<String>();
			for (CommitDate cd : commits) {

				// --判断join迁移
				Movement mm = new Movement();
				mm.setName(cd.getName());
				mm.setMove("join");
				mm.setDate(cd.getDate().replace("-", ""));

				Movement latestMm = getLatestMoveOfOne(cd.getName());// 某开发者最近一次迁移情况

				if (latestMm != null) {
					// if(cd.getName().equals("a45"))
					// {
					// System.out.println(latestMm.getDate());
					// }
					// 之前参加项目
					if (latestMm.getMove().equals("leave")) {
						movements.add(mm);
						// 是leave就加入，如果之前是join无须加入
					}
				} else {
					movements.add(mm);
					// 之前没有参加项目则加入
				}

				if (!developersInVersion.contains(cd.getName()))
					developersInVersion.add(cd.getName());
			}
			developerSizeInVersion.add(developersInVersion.size());
			// --判断leave迁移

			ArrayList<String> developersNotInVersion = new ArrayList<String>();
			developersNotInVersion = developers;
			developersNotInVersion.removeAll(developersInVersion);// 获得此版本没有加入开发的开发者

			for (String developer : developersNotInVersion) {

				// 如果是第一个版本不存在leave于否
				if (getVersionDateNum(release) != 0) {
					// 首先开发者参与了才能离开
					if (isJoined(developer)) {
						Movement mm = new Movement();
						mm.setName(developer);
						mm.setMove("leave");
						mm.setDate(versions.get(i - 1).getDate()
								.replace("-", ""));
						movements.add(mm);
					}
				}
			}

		}
		JSONArray json = JSONArray.fromObject(movements);
		String jsonStr = json.toString();

		// for (Movement mm : movements) {
		// System.out.println(mm.getName() + ":" + mm.getMove() + ";"
		// + mm.getDate());
		// }
		return jsonStr;

	}

	/**
	 * 判断时间是否在两个版本发布时间之间，也就是属于某个版本
	 * 
	 * @param prevd
	 * @param vd
	 * @return
	 */
	private boolean dateIsBetweenTwoVersions(VersionDate prevd, VersionDate vd,
			String date) {
		boolean result = false;
		if (DateIsGreater(vd.getDate(), date)
				&& DateIsGreater(date, prevd.getDate())
				&& (!date.equals(prevd.getDate())))// 如果发布时间先于等于传入 且
													// 传入时间先于前一个版本发布时间 且
													// 传入时间不等于前一个版本发布时间
		{
			result = true;
		}
		return result;
	}

	/**
	 * try to get the count of Comments in One release
	 * 
	 * @param projectName
	 * @param releaseName
	 * @return
	 */
	private int getCommentCount(String projectName, String releaseName) {
		int commentCount = 0;
		ArrayList<Comment> comentList = new ArrayList<Comment>();
		comentList = dh.getComments(projectName);
		if (releaseName.equals(versionDates.get(0).getVersion())) {// 如果是第一个版本，取第个版本之前的时间（包含）
			VersionDate vd = new VersionDate();
			vd = getVersionDate(releaseName);
			for (Comment comment : comentList) {
				if (DateIsGreater(vd.getDate(), comment.getDate()))
					commentCount++;
			}

		} else {

			VersionDate vd = new VersionDate();
			vd = getVersionDate(releaseName);
			VersionDate prevd = new VersionDate();
			prevd = getPreVersionDate(releaseName);

			for (Comment comment : comentList) {
				if (dateIsBetweenTwoVersions(prevd, vd, comment.getDate()))
					commentCount++;
			}
		}

		return commentCount;
	}

	/**
	 * try to get the count of Issues in One release
	 * 
	 * @param projectName
	 * @param releaseName
	 * @return
	 */
	private int getIssueCount(String projectName, String releaseName) {
		int issueCount = 0;
		ArrayList<usefuldata.Issue> issueList = new ArrayList<usefuldata.Issue>();
		issueList = dh.getIssues(projectName);
		if (releaseName.equals(versionDates.get(0).getVersion())) {// 如果是第一个版本，取第个版本之前的时间（包含）
			VersionDate vd = new VersionDate();
			vd = getVersionDate(releaseName);
			for (usefuldata.Issue issue : issueList) {
				if (DateIsGreater(vd.getDate(), issue.getInjectedDate()))
					issueCount++;

			}
		} else {// 不是第一个版本，则都取与之前版本（不包含）之间的时间

			VersionDate vd = new VersionDate();
			vd = getVersionDate(releaseName);
			VersionDate prevd = new VersionDate();
			prevd = getPreVersionDate(releaseName);

			for (usefuldata.Issue issue : issueList) {
				if (dateIsBetweenTwoVersions(prevd, vd, issue.getInjectedDate()))
					issueCount++;

			}

		}
		return issueCount;
	}

	private String getRadarJson(String release) {
		ArrayList<Version> versions = new ArrayList<Version>();

		int maxDeveloperSize = 0;
		int maxCodes = 0;
		int maxCommits = 0;
		int maxComments = 0;
		int maxIssues = 0;

		ArrayList<CommitDate> commits = new ArrayList<CommitDate>();
		ArrayList<VersionDate> versiondates = new ArrayList<VersionDate>();
		versiondates = getVersionDatesTillNow(release);// 得到包括请求在内，往后的所有开发版本号
		versiondates = orderVersions(versiondates);// 对其发布顺序从前到后排序
		int j = 0;
		

		for (; j < versiondates.size(); j++) {

			// 计算issue comment commit codes developers 指标最大量
			allCommits = dh.getCommits(projectName);

			if (getVersionDateNum(versiondates.get(j).getVersion()) == 0) {// 如果请求的版本号是第一个版本，选返回所有commits，再减去第一个版本发布时间后的所有commits
				commits = allCommits;
				VersionDate versiondate = new VersionDate();
				versiondate = versiondates.get(j);
				ArrayList<CommitDate> commitsToRemove = new ArrayList<CommitDate>();
				commitsToRemove = getCommitFromVersionToNow(versiondate
						.getVersion());
				commits.removeAll(commitsToRemove);

			} else {// 否则返回在之前的一个版本发布时间往后的所有commit,再减去所请求版本发布时间后的所有commits

				VersionDate versiondate = new VersionDate();
				int versionNum = getVersionDateNum(versiondates.get(j)
						.getVersion());
				versiondate = versionDates.get(versionNum - 1);
				commits = getCommitFromVersionToNow(versiondate.getVersion());
				versiondate = new VersionDate();
				versiondate = versiondates.get(j);
				ArrayList<CommitDate> commitsToRemove = new ArrayList<CommitDate>();
				commitsToRemove = getCommitFromVersionToNow(versiondate
						.getVersion());
				commits.removeAll(commitsToRemove);

			}

			String thisRelease = versiondates.get(j).getVersion();
			if (developerSizeInVersion.get(j) > maxDeveloperSize)
				maxDeveloperSize = developerSizeInVersion.get(j);

			if (dh.getCodes(projectName, thisRelease) > maxCodes)
				maxCodes = dh.getCodes(projectName, thisRelease);

			if (commits.size() > maxCommits)
				maxCommits = commits.size();

			if (getCommentCount(projectName, thisRelease) > maxComments)
				maxComments = getCommentCount(projectName, thisRelease);

			if (getIssueCount(projectName, thisRelease) > maxIssues)
			{
				
				maxIssues = getIssueCount(projectName, thisRelease);
//				System.out.println(thisRelease+":");
//				System.out.println("maxIssues:"+maxIssues);
//				System.out.println("Issues:"+getIssueCount(projectName, thisRelease));
			}

		
		}
		

		int i = 0;
		for (; i < versiondates.size(); i++) {
        
			allCommits = dh.getCommits(projectName);

			if (getVersionDateNum(versiondates.get(i).getVersion()) == 0) {// 如果请求的版本号是第一个版本，选返回所有commits，再减去第一个版本发布时间后的所有commits
				commits = allCommits;
				VersionDate versiondate = new VersionDate();
				versiondate = versiondates.get(i);
				ArrayList<CommitDate> commitsToRemove = new ArrayList<CommitDate>();
				commitsToRemove = getCommitFromVersionToNow(versiondate
						.getVersion());
				commits.removeAll(commitsToRemove);

			} else {// 否则返回在之前的一个版本发布时间往后的所有commit,再减去所请求版本发布时间后的所有commits

				VersionDate versiondate = new VersionDate();
				int versionNum = getVersionDateNum(versiondates.get(i)
						.getVersion());
				versiondate = versionDates.get(versionNum - 1);
				commits = getCommitFromVersionToNow(versiondate.getVersion());
				versiondate = new VersionDate();
				versiondate = versiondates.get(i);
				ArrayList<CommitDate> commitsToRemove = new ArrayList<CommitDate>();
				commitsToRemove = getCommitFromVersionToNow(versiondate
						.getVersion());
				commits.removeAll(commitsToRemove);

			}

			VersionDate vd = new VersionDate();
			vd = versiondates.get(i);

			Version v = new Version();
			v.setDate(vd.getDate().replace("-", ""));
			v.setName(vd.getVersion());
			List<Radar> radars = new ArrayList<Radar>();
			String thisRelease = vd.getVersion();
			int developerValue = 0;
			if (maxDeveloperSize != 0)
				developerValue = (10 * developerSizeInVersion.get(i)
						/ maxDeveloperSize);
			int codeValue = 0;
			if (maxCodes != 0)
				codeValue = (10 * dh.getCodes(projectName, thisRelease)
						/ maxCodes);
			int commitValue = 0;
			if (maxCommits != 0)
				commitValue = (10 * commits.size() / maxCommits);

			int commentValue = 0;
			if (maxComments != 0)
				commentValue = (10 * getCommentCount(projectName, thisRelease) / maxComments);
			int issueValue = 0;
			if (maxIssues != 0)
				issueValue = (10 * getIssueCount(projectName, thisRelease) / maxIssues);
//			System.out.println(thisRelease+":");
//			System.out.println("maxIssues:"+maxIssues);
//			System.out.println("Issues:"+getIssueCount(projectName, thisRelease));
//			System.out.println("issueValue:"+issueValue);

			radars.add(0, new Radar("developer", developerValue));
			// System.out.println(developerSizeInVersion.get(i));
			radars.add(1, new Radar("code", codeValue));
			radars.add(2, new Radar("commit", commitValue));
			radars.add(3, new Radar("comment", commentValue));
			radars.add(4, new Radar("issue", issueValue));

			// radars.add(1,"code",c)
			List<List<Radar>> radarL = new ArrayList<List<Radar>>();

			radarL.add(radars);
			v.setRadar(radarL);
			versions.add(v);

		}

		JSONArray json = JSONArray.fromObject(versions);
		String jsonStr = json.toString();
		// developerSizeInVersion = new ArrayList<Integer>();
		return jsonStr;
	}

	public String getEvolveJson(String release) {
		String movementsJson = getMovementsJson(release);

	String nodesJson = getNodesJson(release);
	String linksJson = getLinksJson(release);
		String radarJson = getRadarJson(release);

		versionDates = dh.getVersions(projectName);
		versionDates = orderVersions(versionDates);

		allCommits = dh.getCommits(projectName);

		developerSizeInVersion = new ArrayList<Integer>();

		developers = dh.getAllDeveloperNames(projectName);
		// releases = dh.getReleaseNames(projectName);
		movements = new ArrayList<Movement>();

		return "{nodes:" + nodesJson + ",links:" + linksJson + ",movements:"
				+ movementsJson + ",version:" + radarJson + "}";

	}

	/*
	 * public static void main(String[] args) { for(int i=0;i<100;i++) {
	 * System.out.println(i); EvolveAnalysis ea = new
	 * EvolveAnalysis("projecName"); System.out.println(ea.getEvolveJson()); } }
	 */

	public static void main(String[] args) {

		DataHelper dh=new DataHelperImpl();
		ArrayList<VersionDate> versionDates = dh.getVersions("mct");
		EvolveAnalysis ea = new EvolveAnalysis("mct");
		for(VersionDate vd:versionDates)
		{
		System.out.println(ea.getEvolveJson(vd.getVersion()));
		}

	}
}

