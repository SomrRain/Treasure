package com.feicuiedu.treasure.treasure.home.map;

import com.feicuiedu.treasure.net.NetClient;
import com.feicuiedu.treasure.treasure.Area;
import com.feicuiedu.treasure.treasure.Treasure;
import com.feicuiedu.treasure.treasure.TreasureApi;
import com.feicuiedu.treasure.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CC on 2016/11/11.
 */
public class MapPresenter {
    private MapMvpView mapMvpView;
    private Area area;

    public MapPresenter(MapMvpView mapMvpView) {
        this.mapMvpView = mapMvpView;
    }

    public void getTreasure(Area area) {
        if (TreasureRepo.getInstance().isCached(area)) {
            return;
        }
        this.area = area;
        TreasureApi treasureApi = NetClient.getInstance().getTreasureApi();
        Call<List<Treasure>> treasureCall = treasureApi.getTreasureInArea(area);

        treasureCall.enqueue(callback);
    }

    private Callback<List<Treasure>> callback = new Callback<List<Treasure>>() {
        @Override
        public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
            if (response.isSuccessful()){
                List<Treasure>  treasureList=response.body();
                if(treasureList==null){
                    mapMvpView.showMessage("警告警告！");
                    return;
                }
                TreasureRepo.getInstance().addTreasure(treasureList);
                TreasureRepo.getInstance().cache(area);
                mapMvpView.setData(treasureList);
            }
        }

        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
            mapMvpView.showMessage(t.getMessage());

        }
    };
}
