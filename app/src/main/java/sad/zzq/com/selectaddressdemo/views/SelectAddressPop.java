package sad.zzq.com.selectaddressdemo.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import sad.zzq.com.selectaddressdemo.callback.AddressCallBack;
import sad.zzq.com.selectaddressdemo.adapter.AddressListAdapter;
import sad.zzq.com.selectaddressdemo.fragment.TownFragment;
import sad.zzq.com.selectaddressdemo.manager.AddressManager;
import sad.zzq.com.selectaddressdemo.MainActivity;
import sad.zzq.com.selectaddressdemo.R;
import sad.zzq.com.selectaddressdemo.callback.TabOnClickListener;
import sad.zzq.com.selectaddressdemo.fragment.CityFragment;
import sad.zzq.com.selectaddressdemo.fragment.DistrictFragment;
import sad.zzq.com.selectaddressdemo.fragment.ProvinceFragment;
import sad.zzq.com.selectaddressdemo.tool.DensityUtils;
import sad.zzq.com.selectaddressdemo.tool.StringUtils;

import static android.R.attr.tag;

/**
 * Created by zzq on 16/9/26.
 */
public class SelectAddressPop extends DialogFragment implements AddressCallBack {
    private MainActivity.SelectAddresFinish mSelectAddresFinish;
    private Context context;
    private View view;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerTab;
    private FrameLayout popBg;

    private AddressManager.Province province;
    private AddressManager.City city ;
    private AddressManager.District district;
    private AddressManager.Town town;
    private String defutText;

    private ProvinceFragment mProvinceFragment;
    private CityFragment mCityFragment;
    private DistrictFragment mDistrictFragment;
    private TownFragment mTownFragment;
    private AddressManager addressManager = AddressManager.newInstance();

    public void setSelectAddresFinish(MainActivity.SelectAddresFinish mSelectAddresFinish) {
        this.mSelectAddresFinish = mSelectAddresFinish;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        initView();
        Dialog dialog = new Dialog(context, R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        addressManager.clear();
    }

    public void setAddress(String pCode, String cCode, String aCode){
        if (StringUtils.isNoEmpty(pCode) && StringUtils.isNoEmpty(cCode) && StringUtils.isNoEmpty(aCode)){
            province = addressManager.findProvinceByCode(pCode);
            city = province.findCityByCode(cCode);
            district = city.findDistrictByCode(aCode);
        }
    }
    public void setAddress(String pCode, String cCode, String aCode,String tCode){
        if (StringUtils.isNoEmpty(pCode) && StringUtils.isNoEmpty(cCode) && StringUtils.isNoEmpty(aCode)){
            province = addressManager.findProvinceByCode(pCode);
            city = province.findCityByCode(cCode);
            district = city.findDistrictByCode(aCode);
            if(StringUtils.isNoEmpty(tCode)){
                town = district.findTownByCode(tCode);
            }
        }

    }
    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.select_address_pop_layout,null);
        ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        pagerTab = (PagerSlidingTabStrip) view.findViewById(R.id.pagerTab);
        popBg = (FrameLayout)view.findViewById(R.id.popBg);
        defutText = context.getString(R.string.selset_birthday);
        pagerTab.setTextSize(DensityUtils.sp2px(context, 14));
        pagerTab.setSelectedColor(getResources().getColor(R.color.new_redbg));
        pagerTab.setTextColor(getResources().getColor(R.color.regis_account_exist));

        List<View> lis = new ArrayList<View>();
        mProvinceFragment = new ProvinceFragment(context,this);
        mCityFragment = new CityFragment(context,this);
        mDistrictFragment = new DistrictFragment(context,this);
        mTownFragment = new TownFragment(context,this);
        lis.add(mProvinceFragment.getListview());
        lis.add(mCityFragment.getListview());
        lis.add(mDistrictFragment.getListview());
        lis.add(mTownFragment.getListview());
        viewPager.setAdapter(new AddressListAdapter(lis));

        String[] addres = null;
        if (province != null && city != null && district != null){
            addres = new String[]{province.getName(),city.getName(),district.getName()};
            if(town != null){
                addres = new String[]{province.getName(),city.getName(),district.getName(),town.getName()};
                mProvinceFragment.setCode(province.getCode());
                mCityFragment.setCode(province.getCode(),city.getCode());
                mDistrictFragment.setCode(province.getCode(),city.getCode(),district.getCode());
                mTownFragment.setCode(province.getCode(),city.getCode(),district.getCode(),town.getCode());
                viewPager.setCurrentItem(3);
                pagerTab.setTabsText(addres);
                pagerTab.setCurrentPosition(3);
            }else{
                mProvinceFragment.setCode(province.getCode());
                mCityFragment.setCode(province.getCode(),city.getCode());
                mDistrictFragment.setCode(province.getCode(),city.getCode(),district.getCode());
                viewPager.setCurrentItem(2);
                pagerTab.setTabsText(addres);
                pagerTab.setCurrentPosition(2);
            }
        }else{
            addres = new String[]{defutText};
            viewPager.setCurrentItem(0);
            pagerTab.setTabsText(addres);
            pagerTab.setCurrentPosition(0);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        pagerTab.setTabOnClickListener(new TabOnClickListener() {
            @Override
            public void onClick(View tab, int position) {
                if (defutText.equals(pagerTab.getTabs()[position])){
                    return;
                }
                viewPager.setCurrentItem(position);
                String[] addres = null;
                switch (position){
                    case 0:
                        if(town != null){
                            addres = new String[]{province.getName(), city.getName(),district.getName(),town.getName()};
                        }
                        else if (district != null) {
                            addres = new String[]{province.getName(), city.getName(),district.getName(),defutText};
                        }else if(city != null) {
                            addres = new String[]{province.getName(), city.getName(), defutText};
                        }else{
                            addres = new String[]{province.getName(), defutText};
                        }
                        break;
                    case 1:
                        if(town != null){
                            addres = new String[]{province.getName(), city.getName(),district.getName(),town.getName()};
                        }
                        else if (district != null) {
                            addres = new String[]{province.getName(), city.getName(),district.getName(), defutText};
                        }else {
                            addres = new String[]{province.getName(), city.getName(), defutText};
                        }
                        //mDistrictFragment.resetIndex();
                        break;
                    case 2:
                        if(town != null){
                            addres = new String[]{province.getName(), city.getName(),district.getName(),town.getName()};
                        }else {
                            addres = new String[]{province.getName(), city.getName(), district.getName(), defutText};
                        }
                        break;
                }
                pagerTab.setTabsText(addres);
                pagerTab.setCurrentPosition(position);
            }
        });

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //KeyBoardUtils.closeKeybord((Activity) context);
        super.show(manager, tag);
    }

    @Override
    public void selectProvince(AddressManager.Province province) {
        String[] addres = new String[]{province.getName(),defutText};
        pagerTab.setTabsText(addres);
        pagerTab.setCurrentPosition(1);
        viewPager.setCurrentItem(1);
        if(province != this.province){
            city = null;
            district = null;
            town = null;
        }
        this.province = province;
        mCityFragment.setCode(province.getCode(),null);
    }

    @Override
    public void selectCity(AddressManager.City city) {
        String[] addres = new String[]{province.getName(),city.getName(),defutText};
        pagerTab.setTabsText(addres);
        pagerTab.setCurrentPosition(2);
        viewPager.setCurrentItem(2);
        if(city != this.city){
            district = null;
            town = null;
        }
        this.city = city;
        mDistrictFragment.setCode(province.getCode(),city.getCode(),null);
    }

    @Override
    public void selectDistrict(AddressManager.District district) {

        //判断是否有下级
        List<AddressManager.Town>  townList = AddressManager.newInstance().findProvinceByCode(province.getCode())
                .findCityByCode(city.getCode()).findDistrictByCode(district.getCode()).getAllTowns();
        if(townList.size()<1){

            this.district = district;
            String[] addres = new String[]{province.getName(),city.getName(),district.getName()};
            pagerTab.setTabsText(addres);
            mSelectAddresFinish.finish(province.getCode(),city.getCode(),district.getCode(),null);
            dismiss();
        }else{
            String[] addres = new String[]{province.getName(),city.getName(),district.getName(),defutText};
            pagerTab.setTabsText(addres);
            pagerTab.setCurrentPosition(3);
            viewPager.setCurrentItem(3);
            if(district != this.district){
                town = null;
            }
            this.district = district;
            mTownFragment.setCode(province.getCode(),city.getCode(),district.getCode(),null);
        }


    }

    @Override
    public void selectTown(AddressManager.Town town) {
        this.town = town;
        String[] addres = new String[]{province.getName(),city.getName(),district.getName(),town.getName()};
        pagerTab.setTabsText(addres);
        mSelectAddresFinish.finish(province.getCode(),city.getCode(),district.getCode(),town.getCode());
        dismiss();
    }


}
