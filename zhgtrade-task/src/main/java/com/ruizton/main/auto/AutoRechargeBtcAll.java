package com.ruizton.main.auto;

import com.ruizton.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.model.BTCInfo;
import com.ruizton.main.model.BTCMessage;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualaddress;
import com.ruizton.main.model.Fvirtualcaptualoperation;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.BTCUtils;
import com.ruizton.util.DateUtils;
import com.ruizton.util.Utils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AutoRechargeBtcAll {
	private static final Logger log = LoggerFactory
			.getLogger(AutoRechargeBtcAll.class);
	@Autowired
	private RechargeBtcData rechargeBtcData ;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService ;
	@Autowired
	private RealTimeData realTimeData ;
	@Autowired
	private FrontUserService frontUserService ;
	private boolean m_sync_flag = false ;

	public void work() {
//		System.out.println("AutoRechargeBtcAll work invoke");

		synchronized (this) {
//			System.out.println("AutoRechargeBtcAll work execute");

			try{
						//遍历现有的
						List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
						//获取钱包中新数据
						for (Fvirtualcointype fvirtualcointype : fvirtualcointypes) {
							try{
								int begin = 0 ;
								int step = 1000 ;
								boolean is_continue = true ;

								if(fvirtualcointype==null || fvirtualcointype.getFstatus()== VirtualCoinTypeStatusEnum.Abnormal){
									continue ;
								}


//								System.out.println("all wallet loop begin on ip = " + fvirtualcointype.getFip() + " , port = " + fvirtualcointype.getFport());

								BTCMessage btcMessage = new BTCMessage() ;
								btcMessage.setACCESS_KEY(fvirtualcointype.getFaccess_key()) ;
								btcMessage.setIP(fvirtualcointype.getFip()) ;
								btcMessage.setPORT(fvirtualcointype.getFport()) ;
								btcMessage.setSECRET_KEY(fvirtualcointype.getFsecrt_key()) ;

								String firstAddress = null ;
								BTCUtils btcUtils = new BTCUtils(btcMessage) ;
								List<BTCInfo> btcInfos = new ArrayList<BTCInfo>() ;

								while(is_continue){
									try {
										btcInfos = btcUtils.listtransactionsValue(step, begin) ;
										begin+=step ;
										if(firstAddress==null && btcInfos.size()>0){
											firstAddress = btcInfos.get(0).getTxid().trim() ;
										}
//										System.out.println("本次找到记录共："+btcInfos.size());
										if(btcInfos == null || btcInfos.size() == 0){
											is_continue = false ;
										}
									} catch (Exception e1) {
										//e1.printStackTrace();
										is_continue = false ;
										continue ;
									}

									for (BTCInfo btcInfo : btcInfos) {

										String txid = btcInfo.getTxid().trim() ;
//										System.out.println("正在循环读取每一条记录："+txid);
										List<Fvirtualcaptualoperation> fvirtualcaptualoperations = this.frontVirtualCoinService.findFvirtualcaptualoperationByProperties(new String[]{"ftype", "ftradeUniqueNumber"}, new Object[]{VirtualCapitalOperationTypeEnum.COIN_IN, txid});
										if(fvirtualcaptualoperations.size()>0){
//											System.out.println("记录已经存在");
											continue ;
										}

										if(btcInfo.getTime().before(DateUtils.formatDate("2017-01-10")) && this.frontVirtualCoinService.findFvirtualcaptualoperationByProperties(new String[]{"ftype", "ftradeUniqueNumber"}, new Object[]{VirtualCapitalOperationTypeEnum.COIN_OUT, txid}).size() > 0){
											// 注意：2017-01-10之前的所有平台内提现-》充值不能到账，已手动给用户充值，此判断过滤那部分不能自动到账记录，避免重复到账
											continue;
										}

										Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation() ;


										boolean hasOwner = true ;
										String address = btcInfo.getAddress().trim() ;
										List<Fvirtualaddress> fvirtualaddresses = this.frontVirtualCoinService.findFvirtualaddress(fvirtualcointype, address) ;
										if(fvirtualaddresses.size()==0){
											hasOwner = false ;//没有这个地址，充错进来了？没收！
										}else if(fvirtualaddresses.size()>1){
											log.error("Dumplicate Fvirtualaddress for address:"+address+" ,Fvirtualcointype:"+fvirtualcointype.getFid()) ;
											continue ;
										}else{
											Fvirtualaddress fvirtualaddress = fvirtualaddresses.get(0) ;
											Fuser fuser = fvirtualaddress.getFuser() ;
											fvirtualcaptualoperation.setFuser(fuser) ;
										}

										fvirtualcaptualoperation.setFhasOwner(hasOwner) ;
										fvirtualcaptualoperation.setFamount(btcInfo.getAmount()) ;
										fvirtualcaptualoperation.setFcreateTime(new Timestamp(btcInfo.getTime().getTime())) ;
										fvirtualcaptualoperation.setFfees(0F) ;
										fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;

										fvirtualcaptualoperation.setFconfirmations(0) ;
										fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_0) ;

										fvirtualcaptualoperation.setFtradeUniqueNumber(btcInfo.getTxid().trim()) ;
										fvirtualcaptualoperation.setRecharge_virtual_address(btcInfo.getAddress().trim()) ;
										fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN);
										fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype);
//										fvirtualcaptualoperation.setFisPreAudit(false);
										try {
//											System.out.println("成功读取一条记录！");
											this.frontVirtualCoinService.addFvirtualcaptualoperation(fvirtualcaptualoperation) ;
											this.rechargeBtcData.subPut(fvirtualcointype.getFid(), fvirtualcaptualoperation.getFtradeUniqueNumber(), fvirtualcaptualoperation) ;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}//for


								}//while
								if(firstAddress!=null){
									this.rechargeBtcData.setTradeRecordMap(fvirtualcointype.getFid(), firstAddress) ;
								}

//								System.out.println("all loop over " + id);


							}catch(Exception e){
								e.printStackTrace() ;
							}

						}//for



				}catch (Exception e) {
					e.printStackTrace() ;
				}

		}


	}













	//加密
	private static final String KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static Key toKey(byte[] key) throws Exception{
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	private static String encrypt(String data, String key) throws Exception{
		Key k = toKey(Base64.decodeBase64(key.getBytes()));                           //还原密钥
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);              //实例化Cipher对象，它用于完成实际的加密操作
		cipher.init(Cipher.ENCRYPT_MODE, k);                               //初始化Cipher对象，设置为加密模式
		return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes()))); //执行加密操作。加密后的结果通常都会用Base64编码进行传输
	}
	private static String decrypt(String data, String key) throws Exception{
		Key k = toKey(Base64.decodeBase64(key.getBytes()));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);                          //初始化Cipher对象，设置为解密模式
		return new String(cipher.doFinal(Base64.decodeBase64(data.getBytes()))); //执行解密操作
	}
}
