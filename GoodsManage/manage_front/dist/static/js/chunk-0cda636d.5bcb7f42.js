(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0cda636d"],{"25f0":function(e,t,r){"use strict";var a=r("6eeb"),o=r("825a"),n=r("d039"),i=r("ad6d"),s="toString",l=RegExp.prototype,c=l[s],u=n((function(){return"/a/b"!=c.call({source:"a",flags:"b"})})),f=c.name!=s;(u||f)&&a(RegExp.prototype,s,(function(){var e=o(this),t=String(e.source),r=e.flags,a=String(void 0===r&&e instanceof RegExp&&!("flags"in l)?i.call(e):r);return"/"+t+"/"+a}),{unsafe:!0})},4142:function(e,t,r){"use strict";r.r(t);var a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app-container"},[r("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"120px"}},[r("el-form-item",{attrs:{prop:"name",label:"物资名称"}},[r("el-input",{model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),r("el-form-item",{attrs:{prop:"num",label:"物资数量"}},[r("el-input",{model:{value:e.form.num,callback:function(t){e.$set(e.form,"num",t)},expression:"form.num"}})],1),r("el-form-item",{attrs:{prop:"flag",label:"是否重点物资"}},[r("el-select",{attrs:{placeholder:"请选择是或否"},model:{value:e.form.flag,callback:function(t){e.$set(e.form,"flag",t)},expression:"form.flag"}},[r("el-option",{attrs:{label:"是",value:"是"}}),r("el-option",{attrs:{label:"否",value:"否"}})],1)],1),r("el-form-item",{attrs:{prop:"phone",label:"负责人电话"}},[r("el-input",{model:{value:e.form.phone,callback:function(t){e.$set(e.form,"phone",t)},expression:"form.phone"}})],1),r("el-form-item",{attrs:{prop:"file",label:"物资图片"}},[e.isShowUpload?r("el-upload",{ref:"upload",staticClass:"avatar-uploader",attrs:{"show-file-list":!1,"on-success":e.handleAvatarSuccess,"before-upload":e.beforeAvatarUpload,headers:e.headers,limit:1,"on-exceed":e.handleExceed,"auto-upload":!1,"on-change":e.onChange,accept:".jpg,.png,.jpeg",name:"file"}},[r("i",{staticClass:"el-icon-plus avatar-uploader-icon"})]):e._e()],1),e.isShowImgUpload?r("el-row",[r("el-col",{staticStyle:{color:"white"},attrs:{span:6}},[e._v("1")]),r("el-col",{attrs:{span:10}},[r("div",{staticStyle:{position:"relative","background-color":"#f0f3fa"}},[r("el-image",{staticStyle:{width:"100%",height:"100%"},attrs:{src:e.localUrl,"preview-src-list":[e.localUrl],fit:"scale-down"}})],1),r("div",{staticStyle:{padding:"10% 40%"}},[r("el-button",{attrs:{type:"primary",loading:!1,size:"small"},on:{click:e.reLoad}},[e._v("重新上传")])],1)]),r("el-col",{staticStyle:{color:"white"},attrs:{span:4}},[e._v("1")])],1):e._e(),r("el-form-item",[r("el-button",{attrs:{type:"primary"},on:{click:e.onSubmit}},[e._v("提交记录")]),r("el-button",{on:{click:e.onCancel}},[e._v("取消上传")])],1)],1)],1)},o=[],n=(r("ac1f"),r("00b4"),r("a9e3"),r("d3b7"),r("25f0"),r("3ca3"),r("ddb0"),r("2b3d"),r("9861"),r("c405")),i={data:function(){var e=function(e,t,r){if(!t)return new Error("请输入电话号码");var a=/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,o=a.test(t);t=Number(t),"number"!==typeof t||isNaN(t)?r(new Error("请输入电话号码")):(t=t.toString(),t.length<0||t.length>12||!o?r(new Error("手机号码格式如:138xxxx8754")):r())},t=function(e,t,r){if(!t)return new Error("请输入数量");t=Number(t),"number"!==typeof t||isNaN(t)?r(new Error("请正确输入数量")):t<=0?r(new Error("数量应该大于0")):r()};return{isShowImgUpload:!1,isShowUpload:!0,form:{name:"",num:"",flag:"",phone:"",file:""},rules:{name:[{required:!0,message:"请输入物品名称",trigger:"blur"}],num:[{required:!0,message:"请输入数量",trigger:"blur"},{validator:t,trigger:"blur"}],flag:[{required:!0,message:"请选择",trigger:"blur"}],phone:[{required:!0,message:"请输入手机号码",trigger:"blur"},{validator:e,trigger:"blur"}],file:[{required:!0,message:"请上传一张图片",trigger:"blur"}]}}},methods:{clearForm:function(){this.isShowImgUpload=!1,this.isShowUpload=!0,this.$refs.form.resetFields()},onSubmit:function(){var e=this;this.$refs.form.validate((function(t){if(t){console.log("onSubmit this form--\x3e"),console.log(e.form);var r=new FormData;for(var a in e.form)r.append(a,e.form[a]),console.log(e.form[a]+" --\x3e "+r[a]);Object(n["c"])(r).then((function(t){e.$message({message:"添加成功",type:"success"}),e.$router.push({path:"/query/category"}),e.clearForm()}))}}))},onCancel:function(){this.$message({message:"已取消!",type:"warning"}),this.clearForm()},reLoad:function(){this.isShowImgUpload=!1,this.isShowUpload=!0,this.form.file="",this.$refs.upload.clearFiles()},handleExceed:function(){this.$message({type:"error",message:"只能上传1张图片！",duration:3e3})},onChange:function(e){var t=-1!=["image/jpeg","image/png","image/jpg"].indexOf(e.raw.type),r=e.raw.size/1024/1024<5;if(t||this.$message({type:"error",message:"上传的图片只能是 png 或 jpg 格式!",duration:3e3}),r||this.$message({type:"error",message:"上传图片大小不能超过 5MB!",duration:2e3}),!t||!r)return this.form.file="",void this.$refs.upload.clearFiles();var a=null;void 0!=window.createObjectURL?a=window.createObjectURL(e.raw):void 0!=window.URL?a=window.URL.createObjectURL(e.raw):void 0!=window.webkitURL&&(a=window.webkitURL.createObjectURL(e.raw)),this.localUrl=a,this.isShowImgUpload=!0,this.isShowUpload=!1,this.form.file=e.raw,console.log("the file of form is--\x3e"),console.log(this.form.file)},handleAvatarSuccess:function(e,t,r){console.log("从后端返回的response--\x3e"),console.log(e),e&&e.data&&e.data.url?this.$message({type:"success",message:"上传图像成功",duration:2e3}):this.$message.error("上传图像失败！")},beforeAvatarUpload:function(e){var t=-1!=["image/jpeg","image/png"].indexOf(e.type),r=e.size/1024/1024<5;return t||this.$message({type:"error",message:"上传的图片只能是 png 或 jpg 格式!",duration:3e3}),r||this.$message({type:"error",message:"上传图片大小不能超过 5MB!",duration:2e3}),t&&r}}},s=i,l=(r("8a91"),r("2877")),c=Object(l["a"])(s,a,o,!1,null,null,null);t["default"]=c.exports},7156:function(e,t,r){var a=r("861d"),o=r("d2bb");e.exports=function(e,t,r){var n,i;return o&&"function"==typeof(n=t.constructor)&&n!==r&&a(i=n.prototype)&&i!==r.prototype&&o(e,i),e}},"8a91":function(e,t,r){"use strict";r("c791")},a9e3:function(e,t,r){"use strict";var a=r("83ab"),o=r("da84"),n=r("94ca"),i=r("6eeb"),s=r("5135"),l=r("c6b6"),c=r("7156"),u=r("c04e"),f=r("d039"),p=r("7c73"),m=r("241c").f,d=r("06cf").f,g=r("9bf2").f,h=r("58a8").trim,b="Number",w=o[b],v=w.prototype,y=l(p(v))==b,S=function(e){var t,r,a,o,n,i,s,l,c=u(e,!1);if("string"==typeof c&&c.length>2)if(c=h(c),t=c.charCodeAt(0),43===t||45===t){if(r=c.charCodeAt(2),88===r||120===r)return NaN}else if(48===t){switch(c.charCodeAt(1)){case 66:case 98:a=2,o=49;break;case 79:case 111:a=8,o=55;break;default:return+c}for(n=c.slice(2),i=n.length,s=0;s<i;s++)if(l=n.charCodeAt(s),l<48||l>o)return NaN;return parseInt(n,a)}return+c};if(n(b,!w(" 0o1")||!w("0b1")||w("+0x1"))){for(var N,U=function(e){var t=arguments.length<1?0:e,r=this;return r instanceof U&&(y?f((function(){v.valueOf.call(r)})):l(r)!=b)?c(new w(S(t)),r,U):S(t)},x=a?m(w):"MAX_VALUE,MIN_VALUE,NaN,NEGATIVE_INFINITY,POSITIVE_INFINITY,EPSILON,isFinite,isInteger,isNaN,isSafeInteger,MAX_SAFE_INTEGER,MIN_SAFE_INTEGER,parseFloat,parseInt,isInteger".split(","),E=0;x.length>E;E++)s(w,N=x[E])&&!s(U,N)&&g(U,N,d(w,N));U.prototype=v,v.constructor=U,i(o,b,U)}},c405:function(e,t,r){"use strict";r.d(t,"b",(function(){return o})),r.d(t,"c",(function(){return n})),r.d(t,"a",(function(){return i}));var a=r("b775");function o(){return Object(a["a"])({url:"/category/list",method:"get"})}function n(e){return Object(a["a"])({url:"/category/insert",method:"post",headers:{"Content-Type":"multipart/form-data"},data:e})}function i(e){return Object(a["a"])({url:"/category/delete",method:"delete",params:{id:e}})}},c791:function(e,t,r){}}]);