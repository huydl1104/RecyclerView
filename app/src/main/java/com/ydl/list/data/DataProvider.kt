package com.ydl.list.data

import android.content.res.TypedArray
import com.ydl.list.R
import com.ydl.list.app.MyApplication
import java.util.*

object DataProvider {
    private fun getData(): ArrayList<Int>{
        val data = ArrayList<Int>()
        val bannerImage: TypedArray =
            MyApplication.getApp()!!.resources.obtainTypedArray(R.array.image_icons)
        for (i in 0..10) {
            val image = bannerImage.getResourceId(i, R.drawable.icon_222)
            data.add(image)
        }
        bannerImage.recycle()
        return data
    }

    fun getPersonList(size: Int): ArrayList<PersonData> {
        var size = size
        if (size == 0) {
            size = 10
        }
        val arr = ArrayList<PersonData>()
        val data = getData()
        for (i in 0 until size) {
            val person = PersonData()
            person.name = "测试title $i"
            person.image = data[i]
            person.sign = "测试$i"
            arr.add(person)
        }
        return arr
    }


    fun  getAddInfoList(): List<AddInfo>{
        val arr = ArrayList<AddInfo>()
        val data = getData()
        for (i in data.indices ) {
            val ad = AddInfo()
            ad.setDrawable(data[i])
            arr.add(ad)
        }
        return arr
    }

    fun getPersonWithAds(page: Int): List<Any> {
        val arrAll = ArrayList<Any>()
        val arrAd = getAddInfoList()
        var index = 0
        for (person in getPersonList(page)) {
            arrAll.add(person)
            //按比例混合广告
            if (Math.random() < 0.2) {
                arrAll.add(arrAd[index % arrAd.size])
                index++
            }
        }
        return arrAll
    }

    private val VIRTUAL_PICTURE = arrayOf(
        PictureData(566, 800, R.drawable.icon_11),
        PictureData(2126, 1181, R.drawable.icon_22),
        PictureData(1142, 800, R.drawable.icon_33),
        PictureData(550, 778, R.drawable.icon_44),
        PictureData(1085, 755, R.drawable.icon_55),
        PictureData(656, 550, R.drawable.icon_66),
        PictureData(1920, 938, R.drawable.icon_77),
        PictureData(1024, 683, R.drawable.icon_88),
        PictureData(723, 1000, R.drawable.icon_99),
        PictureData(2000, 1667, R.drawable.icon_111),
        PictureData(723, 1000, R.drawable.icon_222)
    )

    fun getPictures(): ArrayList<PictureData>{
        val arrayList = ArrayList<PictureData>()
        arrayList.addAll(listOf(*VIRTUAL_PICTURE))
        return arrayList
    }

    fun getNarrowImage(page: Int): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        if (page == 4) {
            return arrayList
        }
        arrayList.addAll(getData())
        return arrayList
    }

    fun getTags(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("要闻")
        list.add("视频")
        list.add("北京")
        list.add("抗疫")
        list.add("推荐")
        list.add("新时代")
        list.add("娱乐")
        list.add("体育")
        list.add("军事")
        list.add("国际")
        list.add("科技")
        list.add("财经")
        list.add("骑车")
        list.add("科普")
        list.add("数码")
        list.add("图片")
//        list.add("重要的不是你从哪里来，而是你到哪里去，找准方向，继续努力。")
//        list.add("所有的节日都不是为了礼物，而是提醒大家别忘了爱与被爱。")
//        list.add("人生遇到的每个人，出场顺序真的很重要，很多人如果换一个时间认识，就会有不同的结局。")
//        list.add("这个地球上有70亿人，我知道有一个人，会为你爬上月亮")
//        list.add("只要你成为一个废物，就没人能够利用你。")
//        list.add("不要用自己的时间去见证别人的成功，愿你走过的所有弯路，最后都成为美丽彩虹。")
//        list.add("幸运是你，遗憾也是你。")
//        list.add("想不问归期的寻你，又怕风尘仆仆的深情扰了你，你看得到我眼里的银河，可我却只能看到你。")
//        list.add("渐渐懂得 ，人还是要清醒，要识趣，更要懂得适可而止。")
//        list.add("情商高呢，主要是让别人高兴；智商高呢，主要是让自己高兴；智商不高情商也不高呢，主要就是自己不高兴了还不让别人高兴。")
//        list.add("人群冲散了我和你，以及那遥不可及的爱情。")
//        list.add("你以为人生最糟的事情，失去了最爱的人。其实最糟糕的事情却是，你因为太爱一个人而失去了自己。")
//        list.add("过了很久才懂，人生那么短，时间不能都浪费在说反话上。")
//        list.add("我们用零距离的爱，弹奏一曲琴韵笙箫，将生命畅想成天籁之音。")
//        list.add("不要做廉价的自己，不要随意去付出，不要一厢情愿去迎合别人。")
//        list.add("与其用泪水悔恨今天，不如用汗水拼搏今天。")
//        list.add("庆幸遇到了你，但也只能是遇见了。")
//        list.add("生活里需要美丽，需要惊喜，更需要你。")
//        list.add("煎茶坐看梨门雨，情话是你，风景也是你。")
//        list.add("后来才知道，好朋友不是通过努力争取来的，而是在各自的道路上奔跑时遇见的。")
//        list.add("认定了的事，要永远执着的，充满勇气的做下去。")
        return list
    }
}