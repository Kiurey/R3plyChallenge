public class provider{

    private int ProviderPackQuantity;
    private float ProviderPackCost;
    private int[] ProviderPackServices;
    private int[] ProviderPackLatency;
    private int ProviderCoordValue;
    private int RegionCoordValue;

    public provider(int PackQuantity, float PackCost, int[] Services, int[] Latency, int ProviderCoord, int RegionCoord){
        ProviderPackQuantity = PackQuantity;
        ProviderPackCost = PackCost;
        ProviderPackServices = Services;
        ProviderPackLatency = Latency;

        ProviderCoordValue = ProviderCoord;
        RegionCoordValue = RegionCoord;
    }

    public int getProviderPackQuantity() {
        return ProviderPackQuantity;
    }

    public int getProviderPackQuantityOfItem(int ItemNo) {
        return ProviderPackServices[ItemNo];
    }

    public float getProviderPackCost() {
        return ProviderPackCost;
    }

    public int[] getProviderPackServices() {
        return ProviderPackServices;
    }

    public int[] getProviderPackLatency() {
        return ProviderPackLatency;
    }
    public int getProviderPackLatencyOfCountry(int Country){
        return ProviderPackLatency[Country];
    }
    public void removeOnePack(){
        ProviderPackQuantity--;
    }

    public int getProviderCoordValue() {
        return ProviderCoordValue;
    }

    public int getRegionCoordValue() {
        return RegionCoordValue;
    }
}