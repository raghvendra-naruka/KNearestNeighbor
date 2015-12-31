import java.util.*;
import java.io.*;

class KNN_Example{
	int n;
	int x1;
	int x2;
	String y;
	double d;

}
public class K_Nearest_Neighbours {

	public static void main(String[] args) throws FileNotFoundException, IOException{

		File ip1 = new File(System.getProperty("user.dir")+"/src/file1.txt");
		File op = new File(System.getProperty("user.dir")+"/src/OP.txt");
		BufferedWriter buf=new BufferedWriter(new FileWriter(op));

		Scanner sc=new Scanner(ip1);

		int k=sc.nextInt();
		int m=sc.nextInt();
		int t=sc.nextInt();

		int[][] perm = new int[t][];
		for (int i = 0; i < t; i++)
		{
			perm[i] = new int[m];
		}


		int j=0;
		int i=0;
		while(sc.hasNext()){
			perm[i][j]=sc.nextInt();
			j++;
			if(j%m==0){
				i++;
				j=0;
			}
		} 

		for (int x = 0; x < t; x++)
		{
			for(int y=0;y<m;y++){
				//System.out.print(permutations[x][y]+" ");
			}
			//System.out.println("");
		}

		File ip2 = new File(System.getProperty("user.dir")+"/src/file2.txt");
		sc=new Scanner(ip2);

		int row=sc.nextInt();
		int col=sc.nextInt();


		List<KNN_Example> intialExampleList=new ArrayList<>();
		KNN_Example e;
		i=0;
		j=0;
		int count=0;
		while(sc.hasNext()){

			e=new KNN_Example();

			e.n=count;
			count++;
			e.x1=j;
			e.x2=i;
			e.y=sc.next();
			intialExampleList.add(e);
			j++;
			if(j%col==0){
				i++;
				j=0;

			}

		}

		List<KNN_Example> dataExampleList=new ArrayList<>();
		i=0;
		for(KNN_Example ea:intialExampleList){
			if(ea.n%col==0){
				// System.out.println("");
			}

			if(!ea.y.equals(".")){

				KNN_Example ex=new KNN_Example();
				ex.x1=ea.x1;
				ex.x2=ea.x2;
				ex.y=ea.y;
				ex.n=i++;
				dataExampleList.add(ex);
			}
			// System.out.print(ea.y+" ");
		}

		for(KNN_Example eb:dataExampleList){
			//System.out.println(eb.y);
		}



		for(int g=1;g<=5;g++){

			System.out.println("\n---------------itration "+g+"----------------------------");

			int h;    

			List<Double> ovError=new ArrayList<>();
			for(h=0;h<perm.length;h++){

				List<KNN_Example> hList=new ArrayList<>();
				List<Integer> er=new ArrayList<>();


				for(j=0;j<perm[0].length;j++){
					hList.add(dataExampleList.get(perm[h][j]));   
				}


				System.out.println("\n List of Permutations");
				for(KNN_Example ec:hList)  
					System.out.print(ec.y+" ");

				List<List<KNN_Example>> subLists=new ArrayList<>();
				List<Integer> tmpList=new ArrayList<>();

				int ktemp=k;
				int mtemp=m;
				int selectedObjs=0;
				int floortmp;
				i=0;
				int s;
				while(mtemp>0){
					double temp = (double)mtemp/(double)ktemp;
					floortmp= (int)Math.floor(temp);
					if(temp>floortmp){
						selectedObjs=(int)Math.ceil(temp);
						mtemp=mtemp-selectedObjs;

					}else{
						selectedObjs=(int)temp;
						mtemp=mtemp-(int)temp;  
					}
					s=i+selectedObjs;
					tmpList.add(s-i);
					i=s;
					ktemp--;
				}

				int high=1; 
				int low=0;
				boolean flag=true;
				for(int o=1;o<tmpList.size();o++){
					if(tmpList.get(o)-tmpList.get(o-1)==0){
						if(flag)high++;
						else low++;
					}else{
						low=1;
						flag=false;
					}
				}

				if(low>=high)
					Collections.sort(tmpList);

				i=0;
				s=0;
				j=0;
				while(s<tmpList.size()){

					j=j+tmpList.get(s);
					List<KNN_Example> subList=hList.subList(i,j);
					subLists.add(subList);
					i=j;      
					s++;
				}


				List<KNN_Example> testList;
				List<KNN_Example> trainList;
				System.out.println("\n Nearest List");
				j=0;
				int erFold;
				int c=0;
				while(j<subLists.size()){
					List<String> opList=new ArrayList<>();    
					erFold=0;
					testList=new ArrayList<>();
					trainList=new ArrayList<>();

					for(i=0;i<subLists.size();i++){
						if(i==j) {
							testList=subLists.get(i);
						}
						else{
							for(KNN_Example e5:subLists.get(i))
								trainList.add(e5);
						}
					}

					for(KNN_Example ed:testList)
					{    
						int x1=ed.x1;
						int y1=ed.x2;
						List<KNN_Example> nearestExampleList=new ArrayList<>();
						for(KNN_Example ee:trainList){
							int x2=ee.x1;
							int y2=ee.x2;
							double d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
							ee.d=d;
							nearestExampleList.add(ee);
						}

						Collections.sort(nearestExampleList, new Comparator(){
							@Override
							public int compare(Object o1, Object o2) {
								KNN_Example e1=(KNN_Example)o1;
								KNN_Example e2=(KNN_Example)o2;
								if(e1.d > e2.d){
									return 1;
								}
								else if(e1.d < e2.d){
									return -1;
								}

								return 0;
								// it can also return 0, and 1
							}
						});

						int pos=0;
						int neg=0;
						i=0;
						while(i<g)
						{      
							if(i==nearestExampleList.size()) break;
							switch (nearestExampleList.get(i).y) {
							case "+":
								pos++;
								break;
							case "-":
								neg++;
								break;
							}

							i++;
						}


						if(pos>neg){
							System.out.print("+ ");
							opList.add("+");
						}
						else{
							System.out.print("- ");
							opList.add("-");
						} 
					}

					for(i=0;i<opList.size();i++)
					{  
						if(!hList.get(c).y.equals(opList.get(i)))
						{
							erFold++;
						}

						c++;
					}

					er.add(erFold);  
					j++;
				}         

				double sumerror=0;   
				for(i=0;i<er.size();i++){
					sumerror+=er.get(i);
				}   

				ovError.add((double)sumerror/m);

			}

			double ovErrorSum=0;
			int ovErrorSize=ovError.size();

			for(double d:ovError){
				ovErrorSum+=d;
			}
			double b=(double)ovErrorSum/ovErrorSize;
			System.out.println("\ne"+g+":"+b);


			double temp=0;
			for(double d:ovError){
				temp=temp+(d-b)*(d-b);
			}
			temp=temp/(ovErrorSize-1);
			System.out.println("sigma"+g+":"+Math.sqrt(temp));

			buf.write("\nk="+g+" "+"e="+b+" "+"sigma="+Math.sqrt(temp));
			buf.flush();

			part3(intialExampleList, dataExampleList,buf, col, g);

		}    

	}


	static void part3(List<KNN_Example> intialExampleList,List<KNN_Example> dataExList,BufferedWriter bf,int totalcols,int h) throws IOException{


		System.out.println("----------------------------------------------");
		List<KNN_Example> dataExList1=new ArrayList<>();
		dataExList1.addAll(dataExList);
		for(KNN_Example u:intialExampleList){

			String output="";
			if(u.y.equals(".")){

				int x1=u.x1;
				int y1=u.x2;
				List<KNN_Example> nearestExampleList=new ArrayList<>();

				for(KNN_Example e8:dataExList1){

					int x2=e8.x1;
					int y2=e8.x2;
					double d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
					e8.d=d;
					nearestExampleList.add(e8);
				}

				Collections.sort(nearestExampleList, new Comparator(){
					@Override
					public int compare(Object o1, Object o2) {
						KNN_Example e1=(KNN_Example)o1;
						KNN_Example e2=(KNN_Example)o2;
						if(e1.d > e2.d){
							return 1;
						}
						else if(e1.d < e2.d){
							return -1;
						}

						return 0;
						// it can also return 0, and 1
					}
				});

				int pos=0;
				int neg=0;
				int i=0;
				while(i<h)
				{      
					if(i==nearestExampleList.size()) break;
					switch (nearestExampleList.get(i).y) {
					case "+":
						pos++;
						break;
					case "-":
						neg++;
						break;
					}

					i++;
				}

				if(pos>neg){
					output="+";
				}
				else{
					output="-";
				} 

			}else{
				output=u.y;
			}



			if(u.n%totalcols==0){
				System.out.println("");
				bf.write("\n");
			}

			bf.write(output+" ");
			bf.flush();
			System.out.print(output+" ");


		}

	}

}
