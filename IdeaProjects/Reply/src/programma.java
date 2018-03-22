import org.omg.CORBA.Current;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class programma {

    static provider[] Providers;
    static project[] Projects;
    static inputScanner inputData;
    static int[] ProvidersAvaiblePacks;

    static double CurrentMinValue;
    static int[] CurrentWinningCombination;

    public static void main(String[] args) throws IOException {

        //lettura file e creazione array
        Scanner s = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            s = new Scanner(new File("third_adventure.in"));
            //s = new Scanner(new File("test.txt"));

            while (s.hasNext()) {
                list.add(s.next());
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
        String[] stockArr = new String[list.size()];
        stockArr = list.toArray(stockArr);

        //creazione providers e project list
        inputData = new inputScanner(stockArr);
        Providers = inputData.getProvider();
        Projects = inputData.getProjects();
/*
        //ordinamento per oggetti/penalty
        QuickSort sorting = new QuickSort();
        sorting.sort(Projects);
*/
        //ProvidersAvaiblePacks make: pack disponibili per provider
        ProvidersAvaiblePacks = new int[inputData.getTotalProvidersNumber()];
        for (int CurrentProvider = 0; CurrentProvider < inputData.getTotalProvidersNumber(); CurrentProvider++) {
            ProvidersAvaiblePacks[CurrentProvider] = Providers[CurrentProvider].getProviderPackQuantity();
        }
        AlgoritmoMagico();
        //mixedPriorityAlgorithm();
        //ProjectPriorityAlgorithm();
        //packPriorityAlgorithm();

        printResults();


    }//fine main

    static private void printResults() {
        int[] Packets;
        for (int CurrentProject = 0; CurrentProject < inputData.getProjectsQuantity(); CurrentProject++) {
            Packets = Projects[CurrentProject].getBuyList();
            for (int CurrentProv = 0; CurrentProv < inputData.getTotalProvidersNumber(); CurrentProv++) {
                if (Packets[CurrentProv] > 0) {
                    System.out.print(Providers[CurrentProv].getProviderCoordValue() + " " + Providers[CurrentProv].getRegionCoordValue() + " " + Packets[CurrentProv] + " ");
                }
            }
            System.out.println();
        }


    }


    //end print


    //Mixed Algorithm

    static private void mixedPriorityAlgorithm() {

        for (int CurrentProject = 0; CurrentProject < inputData.getProjectsQuantity(); CurrentProject++) {
            double CurrentMaxDifference = 0;
            double CurrentBestScore = 0;
            int BestProv = 0;
            double OldBestScore = -1;
            while (CurrentBestScore != OldBestScore) {
                OldBestScore = CurrentBestScore;
                for (int CurrentProv = 0; CurrentProv < inputData.getTotalProvidersNumber(); CurrentProv++) {
                    if (Providers[CurrentProv].getProviderPackQuantity() > 0 && CheckPackUtility(Projects[CurrentProject], CurrentProv)) {
                        if (!(Projects[CurrentProject].isDone())) {
                            addToBuyList(Projects[CurrentProject], CurrentProv);
                            double TempScore = getExtraScore(Projects[CurrentProject]);
                            if (TempScore > 0) {
                                Projects[CurrentProject].setTempScore(TempScore);
                                double ScoreDifference = Projects[CurrentProject].getScore() - Projects[CurrentProject].getTempScore();
                                if (ScoreDifference < CurrentMaxDifference) {
                                    CurrentMaxDifference = ScoreDifference;
                                    BestProv = CurrentProv;
                                    CurrentBestScore = Projects[CurrentProject].getTempScore();
                                }
                            }
                            takeFromBuyList(Projects[CurrentProject], CurrentProv);

                        }


                    }

                }//end for
                if (CurrentBestScore != OldBestScore) {
                    addToBuyList(Projects[CurrentProject], BestProv);
                    Providers[BestProv].removeOnePack();
                }
            }//end while

        }
    }

    //project algoritm


    static private void ProjectPriorityAlgorithm() {


        for (int CurrentProject = 0; CurrentProject < inputData.getProjectsQuantity(); CurrentProject++) {
            System.out.println(CurrentProject);
            CurrentWinningCombination = Projects[CurrentProject].getCompleteBuyList();
            //chiamata ricorsiva del project
            CurrentMinValue = -1;

            Recursion(Projects[CurrentProject], 0);

            //fine chiamata ricorsiva
            ClassFix(Projects[CurrentProject]);
            //Projects[CurrentProject].setCompleteBuyList(CurrentWinningCombination);

        }

    }

    static private void Recursion(project Project, int RecursionIndex) {
        if (!Project.isDone()) {
            for (int CurrentProv = RecursionIndex; CurrentProv < inputData.getTotalProvidersNumber(); CurrentProv++) {
                if (Providers[CurrentProv].getProviderPackQuantity() > 0 && CheckPackUtility(Project, CurrentProv)) {
                    addToBuyList(Project, CurrentProv);

                    Recursion(Project, CurrentProv);

                    takeFromBuyList(Project, CurrentProv);

                }


            }//end for
        }//end !isDone if
        double Score = getScore(Project);
        if (Score < CurrentMinValue || CurrentMinValue == -1) {
            CurrentMinValue = Score;
            CurrentWinningCombination = Project.getCompleteBuyList();
        }


    }


    static private void ClassFix(project Project) {

        for (int i = 0; i < CurrentWinningCombination.length; i++) {
            for (int j = 0; j < CurrentWinningCombination[i]; j++) {
                addToBuyList(Project, i);
                Providers[i].removeOnePack();
            }
        }
    }

    static private boolean CheckPackUtility(project Project, int PackNo) {


        for (int CurrentService = 0; CurrentService < inputData.getServicesQuantity(); CurrentService++) {
            if ((Project.getProjectServicesLeftToBuyNo(CurrentService) > 0) && Providers[PackNo].getProviderPackQuantityOfItem(CurrentService) > 0) {
                return true;
            }
        }
        return false;
    }

    //Pack Algoritm


    static private void packPriorityAlgorithm() {
        for (int CurrentProv = 0; CurrentProv < inputData.getTotalProvidersNumber(); CurrentProv++) {
            //for cycle for packets
            for (int CurrentPacket = Providers[CurrentProv].getProviderPackQuantity(); CurrentPacket > 0; CurrentPacket--) {
                Providers[CurrentProv].removeOnePack();
                double CurrentMaxDifference = 0;
                int CurrentMaxProject = -1;

                //for cycle for projects
        /*
            -aggiungi item al pacchetto
            -calcola nuovo score
            -score vecchio-score nuovo
            -confronta con max attuale
                -eventuale scrittura
         */
                for (int CurrentProject = 0; CurrentProject < inputData.getProjectsQuantity(); CurrentProject++) {
                    if (!(Projects[CurrentProject].isDone())) {
                        addToBuyList(Projects[CurrentProject], CurrentProv);
                        // if(!(Projects[CurrentProject].isDone())) {
                        //if(isNotAwaste(Projects[CurrentProject])){
                        Projects[CurrentProject].setTempScore(getScore(Projects[CurrentProject]));
                        double ScoreDifference = Projects[CurrentProject].getScore() - Projects[CurrentProject].getTempScore();
                        if (ScoreDifference > CurrentMaxDifference) {
                            CurrentMaxDifference = ScoreDifference;
                            CurrentMaxProject = CurrentProject;
                        }
                        //}


                        takeFromBuyList(Projects[CurrentProject], CurrentProv);
                        // Projects[CurrentProject].isDone();
                    }
                }
                if (CurrentMaxProject >= 0) {
                    addToBuyList(Projects[CurrentMaxProject], CurrentProv);
                    Projects[CurrentMaxProject].setScore(Projects[CurrentMaxProject].getTempScore());
                }
            }
        }
    }

    //check waste

    static private boolean isNotAwaste(project Project) {
        int temp = 0;
        for (int i = 0; i < inputData.getServicesQuantity(); i++) {
            if (Project.getProjectServicesLeftToBuyNo(i) < 0) {
                temp = temp - Project.getProjectServicesLeftToBuyNo(i);
            }
        }
        if (temp > inputData.getAvItems()) {
            return false;
        } else {
            return true;
        }
    }
    //utility: add items bought to project

    static private void addToBuyList(project Project, int PackNo) {
        int[] PackItems = Providers[PackNo].getProviderPackServices();
        Project.addToBuyList(PackNo);
        Project.setProjectServicesLeftToBuy(PackItems);
    }

    //reverse of addToBuyList
    static private void takeFromBuyList(project Project, int PackNo) {
        int[] PackItems = Providers[PackNo].getProviderPackServices();
        Project.takeFromBuyList(PackNo);
        Project.takeProjectServicesLeftToBuy(PackItems);
    }


    //ScoreCalcUtilities
    //TP calc
    //packs*N item per pack

    static private int getTotalItemsFromPacks(int ProviderNo, int PackQuantity) {
        int ItemEachPack = 0;
        for (int i = 0; i < inputData.getServicesQuantity(); i++) {
            ItemEachPack = ItemEachPack + Providers[ProviderNo].getProviderPackQuantityOfItem(i);
        }
        return ItemEachPack * PackQuantity;
    }


    static private float getTpNum(project Project) {
        if (getPacksNum(Project) > 0) {
            float Num = 0;
            float Den = 0;
            for (int ProvNo = 0; ProvNo < inputData.getTotalProvidersNumber(); ProvNo++) {
                Num = Num + Providers[ProvNo].getProviderPackLatencyOfCountry(Project.getProjectCountry()) * getTotalItemsFromPacks(ProvNo, Project.getBuyListOfProv(ProvNo));
            }
            for (int ProvNo = 0; ProvNo < inputData.getTotalProvidersNumber(); ProvNo++) {
                Den = Den + getTotalItemsFromPacks(ProvNo, Project.getBuyListOfProv(ProvNo));
            }
            return Num / Den;
        } else {
            return 0;
        }
    }

    static private float getTpDen(project Project) {
        float Total = 0;
        int Div = 0;
        for (int ItemNo = 0; ItemNo < inputData.getServicesQuantity(); ItemNo++) {
            int Num = 0;
            for (int ProvNo = 0; ProvNo < inputData.getTotalProvidersNumber(); ProvNo++) {
                Num = Num + (
                        Project.getBuyListOfProv(ProvNo) * Providers[ProvNo].getProviderPackQuantityOfItem(ItemNo)
                );
            }
            Num = Num * Num;
            if (Num > 0) {
                int Den = 0;
                int DenTemp = 0;
                for (int ProvNo = 0; ProvNo < inputData.getTotalProvidersNumber(); ProvNo++) {
                    DenTemp = Project.getBuyListOfProv(ProvNo) * Providers[ProvNo].getProviderPackQuantityOfItem(ItemNo);
                    DenTemp = DenTemp * DenTemp;
                    Den = Den + DenTemp;

                }
                Div = Num / Den;
            } else {
                Div = 0;
            }

            Total = Total + Div;

        }
        return Total / inputData.getServicesQuantity();

    }

    static private int getPacksNum(project Project) {
        int PacksNum = 0;
        for (int ProvNo = 0; ProvNo < inputData.getTotalProvidersNumber(); ProvNo++) {
            PacksNum = PacksNum + Project.getBuyListOfProv(ProvNo);
        }
        return PacksNum;
    }

    static private float getPacksPrice(project Project) {
        float Price = 0;
        for (int ProvNo = 0; ProvNo < inputData.getTotalProvidersNumber(); ProvNo++) {
            Price = Price + (
                    Project.getBuyListOfProv(ProvNo) * Providers[ProvNo].getProviderPackCost()
            );
        }
        return Price;
    }

    static private float getTp(project Project) {
        float Tp = 0;
        float TpNum = getTpNum(Project);
        float TpDen = getTpDen(Project);
        Tp = TpNum / TpDen;
        Tp = Tp * getPacksPrice(Project);
        return Tp;
    }


    //FS Calc

    static private double getFs(project Project) {
        double TotalFs = 0;
        int[] ItemsLeft = Project.getProjectServicesLeftToBuy();

        for (int CurrentService = 0; CurrentService < inputData.getServicesQuantity(); CurrentService++) {
            if (ItemsLeft[CurrentService] > 0) {
                TotalFs = TotalFs + (Project.getProjectPenalty() * ((Project.getTargetItem(CurrentService) - ItemsLeft[CurrentService]) / Project.getTargetItem(CurrentService)));
            }
        }
        return TotalFs;
    }

    static private double getScore(project Project) {
        return getFs(Project) + getTp(Project);
    }

    static private double getExtraScore(project Project) {
        return getFs(Project) + getTp(Project) + getExtra(Project);
    }

/*
    static private double getFs(project Project) {
        int TotalItemsLeft = 0;
        int[] ItemsLeft = Project.getProjectServicesLeftToBuy();
        for (int ServNo = 0; ServNo < inputData.getServicesQuantity(); ServNo++) {
            if (ItemsLeft[ServNo] > 0) {
                TotalItemsLeft = TotalItemsLeft + ItemsLeft[ServNo];
            }
        }

        if (TotalItemsLeft > 0) {
            return Project.getPenaltyForItem() * ((Project.getTotalItems() - TotalItemsLeft)
                    / Project.getTotalItems());
        } else {
            return 0;
        }
    }

    static private double getScore(project Project) {
        return getFs(Project) + getTp(Project);
    }

    static private double getExtraScore(project Project) {
        return getFs(Project) + getTp(Project) + getExtra(Project);
    }*/

    static private double getExtra(project Project) {
        int TotalWastedItems = 0;
        int[] ItemsLeft = Project.getProjectServicesLeftToBuy();
        for (int ServNo = 0; ServNo < inputData.getServicesQuantity(); ServNo++) {
            if (ItemsLeft[ServNo] < 0) {
                TotalWastedItems = TotalWastedItems - ItemsLeft[ServNo];
            }
        }

        if (TotalWastedItems < 0) {
            if (TotalWastedItems < inputData.getAvItems()) {
                return inputData.getAvPenalty() * ((inputData.getAvItems() - TotalWastedItems)
                        / inputData.getAvItems());
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }


    static private void AlgoritmoMagico() {
        for (int CurrentProject = 0; CurrentProject < inputData.getProjectsQuantity(); CurrentProject++) {
            while (true) {
                double CurrentMin = Double.MAX_VALUE;
                int MinCoord = -1;
                for (int CurrentProv = 0; CurrentProv < inputData.getTotalProvidersNumber(); CurrentProv++) {
                    if (Providers[CurrentProv].getProviderPackQuantity() > 0) {

                        double CurrentValue = CalcoloMagico(Projects[CurrentProject], CurrentProv);
                        if (CurrentValue < CurrentMin) {
                            CurrentMin = CurrentValue;
                            MinCoord = CurrentProv;
                        }

                    }
                }//end for
                if (CurrentMin == Double.MAX_VALUE) {
                    break;
                }

                addToBuyList(Projects[CurrentProject], MinCoord);
                Providers[MinCoord].removeOnePack();

                if (Projects[CurrentProject].isDone()) {
                    break;
                }


            }//end while
        }
    }

    static private void ProgrammaMagico(project Project) {


    }

    static private double CalcoloMagico(project Project, int ProvNo) {
        addToBuyList(Project, ProvNo);
        int waste = 0;

        for (int CurrentService = 0; CurrentService < inputData.getServicesQuantity(); CurrentService++) {
            if (Project.getProjectServicesLeftToBuyNo(CurrentService) < 0) {
                waste = waste - Project.getProjectServicesLeftToBuyNo(CurrentService);
            }
        }

        double Num = Providers[ProvNo].getProviderPackCost() * Providers[ProvNo].getProviderPackLatencyOfCountry(Project.getProjectCountry());
        double Sum = (1000 * Project.getBuyListOfProv(ProvNo)) + waste * 1000;
        double Fs = getFsMagico(Project);

        takeFromBuyList(Project, ProvNo);

        if (Fs < 0.000001
                ) {
            return Double.MAX_VALUE;
        } else {
            return (Num / Fs) + Sum;
        }
    }

    private static double getFsMagico(project Project) {

        double TotalFs = 0;
        int[] ItemsLeft = Project.getProjectServicesLeftToBuy();

        for (int CurrentService = 0; CurrentService < inputData.getServicesQuantity(); CurrentService++) {
            if (ItemsLeft[CurrentService] > 0) {
                TotalFs = TotalFs + ItemsLeft[CurrentService];
            }
        }


        return 1 - (TotalFs / Project.getTotalItems());
    }














      /*
    private void RecursionMaker(project CurrentProject, int CurrentProvider, int[] CurrentPacks) {
        int[] nextPacks = CurrentPacks.clone();
        int[] CurrentItems = CurrentProject.getProjectServices();

        getCurrentItems(CurrentPacks, CurrentItems);

        if (!checkMissingItem(CurrentProject, CurrentItems)) {

            int PacksCheck = 0;
            int PackCounter = 0;
            for (int i = CurrentProvider; i < inputData.getServicesQuantity(); i++) {
                nextPacks[i]++;
                RecursionMaker(CurrentProject, i, nextPacks);
                nextPacks[i]--;
            }
        }


    }

    private int checkMissingItem(project CurrentProject, int[] currentItems) {
        int temp = 0;

        for (int i = 0; i < inputData.getServicesQuantity(); i++) {
            if ((CurrentProject.getTargetItem(i) - currentItems[i]) < 0) {
                temp++;
            }
        }
        if (temp == inputData.getServicesQuantity()) {
            return 1;
        } else {
            return 0;
        }

    }

    private void getCurrentItems(int[] CurrentPacks, int[] currentItems) {
        for (int i = 0; i < inputData.getServicesQuantity(); i++) {
            for (int j = 0; j < CurrentPacks.length(); j++) {
                currentItems[i] = currentItems[i] - Providers[j].getProviderPackQuantityOfItem(i);
            }
        }
    }


    private int getCurrentScore(project CurrentProject, int[] CurrentPacks) {
        for (int i = 0; i < CurrentPacks.length; i++) {
            if (!checkMissingItem(CurrentProject, CurrentPacks))


        }

    }


    */


}