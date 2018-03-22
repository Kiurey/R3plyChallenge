public class project{

    private int ProjectPenalty, ProjectCountry, PenaltyForItem, TotalItems;
    private int[] ProjectServices;
    private int[] ProjectServicesLeftToBuy;
    private double Score;
    private double TempScore;
    private int[] BuyList;
    private boolean done = false;

    public project(int Penalty, int CountryValue, int[] Services, int ProvidersQuantity){
       ProjectPenalty = Penalty;
       ProjectCountry = CountryValue;
       ProjectServices = Services.clone();
       ProjectServicesLeftToBuy = Services.clone();
       BuyList = new int[ProvidersQuantity];
       calcPenaltyForItem();
       Score = TotalItems*ProjectPenalty;


    }

    private void calcPenaltyForItem(){
        TotalItems = 0;
        for(int i = 0; i < ProjectServices.length; i++){
            TotalItems = TotalItems + ProjectServices[i];
        }

        PenaltyForItem = ProjectPenalty / TotalItems;
    }

    public int getProjectPenalty() {
        return ProjectPenalty;
    }

    public int getProjectCountry() {
        return ProjectCountry;
    }

    public int getPenaltyForItem() {
        return PenaltyForItem;
    }

    public int[] getProjectServices() {
        return ProjectServices;
    }

    public int[] getBuyList() {
        return BuyList;
    }

    public void setBuyList(int ProviderNumber, int PacksAmount) {
        BuyList[ProviderNumber] = PacksAmount;
    }

    public int[] getProjectServicesLeftToBuy() {
        return ProjectServicesLeftToBuy;
    }

    public int getProjectServicesLeftToBuyNo(int No) {
        return ProjectServicesLeftToBuy[No];
    }


    //add 1 pack to project
    public void addToBuyList(int ProviderNumber) {
        BuyList[ProviderNumber]++;
    }

    public void setCompleteBuyList(int[] ProvPacks){
        BuyList = ProvPacks;
    }

    public int[] getCompleteBuyList(){
        return BuyList;
    }

    public void setProjectServicesLeftToBuy(int[] projectServicesLeftToBuy) {
        for(int i = 0; i < ProjectServices.length; i++){
            ProjectServicesLeftToBuy[i]=ProjectServicesLeftToBuy[i] - projectServicesLeftToBuy[i];

        }
    }

    //remove 1 pack from project
    public void takeFromBuyList(int ProviderNumber) {
        BuyList[ProviderNumber]--;
    }

    public void takeProjectServicesLeftToBuy(int[] projectServicesLeftToBuy) {
        for(int i = 0; i < ProjectServices.length; i++){
            ProjectServicesLeftToBuy[i]=ProjectServicesLeftToBuy[i] + projectServicesLeftToBuy[i];

        }
    }

    public int getTargetItem(int i) {
        return ProjectServices[i];
    }

    public int getTotalItems() {
        return TotalItems;
    }

    public int getBuyListOfProv(int PacksNo) {
        return BuyList[PacksNo];
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public double getTempScore() {
        return TempScore;
    }

    public void setTempScore(double score) {
        TempScore = score;
    }



    public boolean isDone(){
        if(!done){
            done=true;
            for(int i = 0; i < ProjectServicesLeftToBuy.length; i++){
                if(ProjectServicesLeftToBuy[i] > 0){
                    done = false;
                }
            }
            return false;
        } else {
            for(int i = 0; i < ProjectServicesLeftToBuy.length; i++){
                if(ProjectServicesLeftToBuy[i] > 0){
                    done = false;
                }
            }
            return true;
        }
    }
}

