package sad.zzq.com.selectaddressdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import sad.zzq.com.selectaddressdemo.manager.AddressManager;
import sad.zzq.com.selectaddressdemo.tool.StringUtils;
import sad.zzq.com.selectaddressdemo.views.SelectAddressPop;


public class MainActivity extends Activity implements View.OnClickListener{

    //收货人
    @ViewInject(R.id.edit_consignee)
    private EditText consignee;
    //手机号
    @ViewInject(R.id.edit_phone_num)
    private EditText phoneNum;
    //区县地址
    @ViewInject(R.id.edit_adress)
    private EditText adress;
    //详细地址
    @ViewInject(R.id.edit_detail_address)
    private EditText detailAddress;


    private String provinceCode ;
    private String cityCode;
    private String areaCode;
    private String townCode;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(rootView);
        x.view().inject(this);
        adress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edit_adress) {
            SelectAddressPop pop = new SelectAddressPop();
            pop.setSelectAddresFinish(new SelectAddresFinish(){

                @Override
                public void finish(String pCode, String cCode, String aCode,String tCode) {
                    //选中地址完成
                    provinceCode = pCode;
                    cityCode = cCode;
                    areaCode = aCode;
                    if(StringUtils.isNoEmpty(tCode)){
                        townCode = tCode;
                        String addr = AddressManager.newInstance().getAddress(pCode, cCode, aCode,tCode);
                        adress.setText(addr);
                    }else {
                        String addr = AddressManager.newInstance().getAddress(pCode, cCode, aCode);
                        adress.setText(addr);
                    }
                }
            });
            pop.setAddress(provinceCode,cityCode,areaCode,townCode);
            pop.show(getFragmentManager(),"address");
        }
    }

    public interface SelectAddresFinish{
        void finish(String provinceCode, String cityCode, String areaCode,String townCode);
    }

}
