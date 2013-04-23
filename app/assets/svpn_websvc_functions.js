var SF_SYS_WEBURLHEAD	="/web/";
var SF_SYS_FTPURLHEAD	="/ftp/";
var SF_SYS_UNKNOENPATH	=0;
var SF_SYS_FTPPATH	  =1;
var SF_SYS_FULLPATH	 =2;
var SF_SYS_ABSPATH	  =3;
var SF_SYS_FULLPATH_1 = 4;
var SF_SYS_REVERSED = 5;//about:|javascript:|vbscript
var SF_SYS_RELPATH	  =10;
var SF_SYS_URL_COM=0;
var SF_SYS_URL_ALL=1;
var SF_SYS_URL_SUB=2;
var SF_TYPE_NOTOBJ	="not_object";
var SF_TYPE_ELEMENT	="element";
var SF_TYPE_UNKNOWN	="object";
var SF_TYPE_WINDOW	="window";
var SF_TYPE_BODY = "body";
var SF_TYPE_DOCUMENT	="document";
var SF_TYPE_LOCATION	="location";
var SF_TYPE_STYLESHEET	="styleSheet";
var SF_TYPE_STYLE = "ele-style";
var SF_TYPE_XMLHTTPREQ	="XMLHttpRequest";
var SF_TYPE_ACTIVEX	="ActiveXObject";
var SF_TYPE_XMLDOCUMENT	="XMLDocument";
var SF_TYPE_OBJECT	="object";
var SF_TYPE_TEXTNODE = "TextNode";
var SF_TYPE_EVENT = "event";

var SF_TYPE_ARG_HTML = 0;
var SF_TYPE_ARG_URL = SF_TYPE_ARG_HTML + 1;
var SF_TYPE_ARG_JS = SF_TYPE_ARG_URL + 1;
var SF_TYPE_ARG_VB = SF_TYPE_ARG_JS + 1;
var SF_TYPE_ARG_CSS = SF_TYPE_ARG_VB + 1;
var SF_TYPE_ARG_UNKNOW = SF_TYPE_ARG_CSS + 1;

var SF_ORIFUNC_SHELP;
var SF_g_svpn_svc_flag;
var SF_g_conofcurloc;
var SF_g_svpnurl = "";
var SF_g_seperator = '@';
var SF_REWRITE_POST_CONTENT="content";
var SF_REWRITE_POST_REFURL="refurl";
var SF_REWRITE_POST_TYPE="type";
var SF_REWRITE_POST_LEN="length";
var SF_REWRITE_ADDR="/rewriter/";
var SF_REWRITE_TYPE_HTML = 1;
var SF_REWRITE_TYPE_JS = SF_REWRITE_TYPE_HTML + 1;
var SF_REWRITE_TYPE_VBS = SF_REWRITE_TYPE_JS + 1;
var SF_REWRITE_TYPE_CSS = SF_REWRITE_TYPE_VBS + 1;
var SF_REWRITE_TYPE_MAX = SF_REWRITE_TYPE_CSS + 1;
var OPTION_FLAG_NONE = 0;
var OPTION_FLAG_DOCUMENT_REPLACE = 100;
var OPTION_FLAG_POPUP_REPLACE = 200;
var SF_IsPageLoaded = false;
var SF_IsTimeOut = 0;

var SF_MARKE_ID = 1;//Make  rewrite objects by id
var SF_MARKE_NAME = SF_MARKE_ID + 1;//Make  rewrite objects by id

var SF_ReWriteObjs=[];//Mark object instances need rewriting
var SF_ReWriteObjsConfig = [];//Object ReWrite configurations.


var SF_OBJECT_LABEL_OBJECT = 0;
var SF_OBJECT_LABEL_APPLET = SF_OBJECT_LABEL_OBJECT + 1;
var SF_OBJECT_LABEL_EMBED  = SF_OBJECT_LABEL_APPLET + 1;
var SF_OBJECT_LABEL_ACTIVEX = SF_OBJECT_LABEL_EMBED + 1;
var SF_OBJECT_LABEL_UNKNOW = SF_OBJECT_LABEL_ACTIVEX + 1;

var SF_REWRITE_INDEX_OBJECT = 0;
var SF_REWRITE_INDEX_CLASSID = SF_REWRITE_INDEX_OBJECT +1;
var SF_REWRITE_INDEX_MARK_FLAG = SF_REWRITE_INDEX_CLASSID + 1;

var SF_REWRITE_CFG_CLASSID = "clasid";
var SF_REWRITE_CFG_MARK_FLAG = "mark_flag";
var SF_REWRITE_CFG_FUN = "fun";
var SF_REWRITE_CFG_ATTR = "attr";

var SF_REWRITE_ACTIVEXID_FLASH =/d27cdb6e-ae6d-11cf-96b8-444553540000/i;
var SF_REWRITE_ACTIVEXID_APPLET1 = /8AD9C840-044E-11D1-B3E9-00805F499D93/i;
var SF_REWRITE_ACTIVEXID_APPLET2 = /CAFEEFAC(-\w{4}){2}-ABCDEFFEDCBA/;

var SF_FAKEURL_FLAG	="\/safeurl";

var INDEX_ATT_NAME = 0;
var INDEX_FILTER_TYPE = INDEX_ATT_NAME + 1;
var INDEX_CALL_FUNCNAME = INDEX_FILTER_TYPE + 1;
var INDEX_URL_POS = INDEX_CALL_FUNCNAME + 1;
var INDEX_ARGS_NUM = INDEX_URL_POS + 1;
var INDEX_OBJ_TYPE = INDEX_ARGS_NUM + 1;
var INDEX_MAX = INDEX_OBJ_TYPE + 1;
var SF_const_scriptkeys = null;
var SF_JS_KEYWORD_ExpObj = null;
var SF_AttachEvent_handles = [];//[[orgHandle1,newHandle1],[orgHandle2,newHandle2],...]

var CACHE_FLAG_NONE = 0;
var CACHE_FLAG_DOCUMENT_REPLACE = 1<<0;
var CACHE_FLAG_POPUP_REPLACE = 1<<1;

var SF_FLS_WRITE = 1;
var SF_FLS_WRITELN = SF_FLS_WRITE + 1;
var SF_FLS_INNERHTML = SF_FLS_WRITELN + 1;
var SF_FLS_ALL = SF_FLS_INNERHTML + 1;
var SF_FLS_MAX = SF_FLS_ALL + 1;

//exrepssion type difer fun,left value,right value
var SF_KEY_TYPE_FUNC = 1;
var	SF_KEY_TYPE_RIGHT = 2;
var	SF_KEY_TYPE_LEFT = 3;
var	SF_KEY_TYPE_LEFT_RIGHT = 4;

if(typeof(SF_g_flahObjs)=='undefined' || !SF_g_flahObjs) //use svpn_websvc_functions.js as block js may overwrite SF_g_flahObjs;
	SF_g_flahObjs = [];


var SF_REF_URL = null;
var SF_g_CacheRewrite ={};
var SF_Util = {};
var SF_g_CookieStamp = 0;
function SF_FUNC_INIT_GlobalVars()
{
	SF_FUNC_RoutineTrace("SF_FUNC_INIT_GlobalVars",arguments);
	if( !(SF_const_scriptkeys == null || typeof(SF_const_scriptkeys) =="undefined" ) )
		return;
	SF_SYS_WEBURLHEAD	="/web/";
	SF_SYS_FTPURLHEAD	="/ftp/";
	SF_SYS_UNKNOENPATH	=0;
	SF_SYS_FTPPATH	  =1;
	SF_SYS_FULLPATH	 =2;
	SF_SYS_ABSPATH	  =3;
	SF_SYS_FULLPATH_1 = 4;
	SF_SYS_REVERSED = 5;

	SF_SYS_RELPATH	  =10;
	SF_SYS_URL_COM=0;
	SF_SYS_URL_ALL=1;
	SF_SYS_URL_SUB=2;
	SF_TYPE_NOTOBJ	="not_object";
	SF_TYPE_ELEMENT	="element";
	SF_TYPE_UNKNOWN	="object";
	SF_TYPE_WINDOW	="window";
	SF_TYPE_DOCUMENT	="document";
	SF_TYPE_LOCATION	="location";
	SF_TYPE_STYLESHEET	="styleSheet";
	SF_TYPE_STYLE = "ele-style";
	SF_TYPE_XMLHTTPREQ	="XMLHttpRequest";
	SF_TYPE_ACTIVEX	="ActiveXObject";
	SF_TYPE_XMLDOCUMENT	="XMLDocument";
	SF_TYPE_OBJECT	="object";
	SF_TYPE_TEXTNODE = "TextNode";
	SF_TYPE_EVENT = "event";
	SF_g_svpnurl = "";
	SF_g_seperator = '@';
	SF_REWRITE_POST_CONTENT="content";
	SF_REWRITE_POST_TYPE="type";
	SF_REWRITE_POST_LEN="length";
	SF_REWRITE_ADDR="/rewriter/";
	SF_REWRITE_POST_REFURL="refurl";
	SF_REWRITE_TYPE_HTML = 1;
	SF_REWRITE_TYPE_JS = SF_REWRITE_TYPE_HTML + 1;
	SF_REWRITE_TYPE_VBS = SF_REWRITE_TYPE_JS + 1;
	SF_REWRITE_TYPE_CSS = SF_REWRITE_TYPE_VBS + 1;
	SF_REWRITE_TYPE_MAX = SF_REWRITE_TYPE_CSS + 1;
	
	SF_TYPE_ARG_HTML = 0;
	SF_TYPE_ARG_URL = SF_TYPE_ARG_HTML + 1;
	SF_TYPE_ARG_JS = SF_TYPE_ARG_URL + 1;
	SF_TYPE_ARG_VB = SF_TYPE_ARG_JS + 1;
	SF_TYPE_ARG_CSS = SF_TYPE_ARG_VB + 1;
	SF_TYPE_ARG_UNKNOW = SF_TYPE_ARG_CSS + 1;
	
	OPTION_FLAG_NONE = 0;
	OPTION_FLAG_DOCUMENT_REPLACE = 100;
	
	
	SF_MARKE_ID = 1;//Make  rewrite objects by id
	SF_MARKE_NAME = SF_MARKE_ID + 1;//Make  rewrite objects by id

	SF_ReWriteObjs=[];//Mark object instances need rewriting
	SF_ReWriteObjsConfig = [];//Object ReWrite configurations.

	SF_OBJECT_LABEL_OBJECT = 0;
	SF_OBJECT_LABEL_APPLET = SF_OBJECT_LABEL_OBJECT + 1;
	SF_OBJECT_LABEL_EMBED  = SF_OBJECT_LABEL_APPLET + 1;
	SF_OBJECT_LABEL_ACTIVEX = SF_OBJECT_LABEL_EMBED + 1;
	SF_OBJECT_LABEL_UNKNOW = SF_OBJECT_LABEL_ACTIVEX + 1;
	
	SF_REWRITE_INDEX_OBJECT = 0;
	SF_REWRITE_INDEX_CLASSID = SF_REWRITE_INDEX_OBJECT +1;
	SF_REWRITE_INDEX_MARK_FLAG = SF_REWRITE_INDEX_CLASSID + 1;
	
	SF_REWRITE_CFG_CLASSID = "clasid";
	SF_REWRITE_CFG_MARK_FLAG = "mark_flag";
	SF_REWRITE_CFG_FUN = "fun";
	SF_REWRITE_CFG_ATTR = "attr";
	SF_REWRITE_ACTIVEXID_FLASH =/d27cdb6e-ae6d-11cf-96b8-444553540000/i;
	SF_REWRITE_ACTIVEXID_APPLET1 = /8AD9C840-044E-11D1-B3E9-00805F499D93/i;
	SF_REWRITE_ACTIVEXID_APPLET2 = /CAFEEFAC(-\w{4}){2}-ABCDEFFEDCBA/i;
	SF_FAKEURL_FLAG	="\/safeurl";

	INDEX_ATT_NAME = 0;
	INDEX_FILTER_TYPE = INDEX_ATT_NAME + 1;
	INDEX_CALL_FUNCNAME = INDEX_FILTER_TYPE + 1;
	INDEX_URL_POS = INDEX_CALL_FUNCNAME + 1;
	INDEX_ARGS_NUM = INDEX_URL_POS + 1;
	INDEX_OBJ_TYPE = INDEX_ARGS_NUM + 1;
	INDEX_MAX = INDEX_OBJ_TYPE + 1;

	
	SF_KEY_TYPE_FUNC = 1;
	SF_KEY_TYPE_RIGHT = 2;
	SF_KEY_TYPE_LEFT = 3;
	SF_KEY_TYPE_LEFT_RIGHT = 4;
	
	
	
	SF_const_scriptkeys = new Array
	(  
		//WARNING:following configureation should keep sync with websvc.conf
		/*	  new Array([0]attName,
						[1]filterType,
						[2]func or call function name,
						[3]the position of url(or args need to be rewrited ) , 
						[4]the limit of the length of arguments or less than 0,
						[5]the type of the object",
						[6]the type of args be rewrited:the position of url(or args need to be rewrited ) SF_TYPE_ARG_XXX",
						[7]KeyWord type,KEY_TYPE_XXX),
		*/
		/*00*/new Array("open",1,"func",0,0,SF_TYPE_WINDOW + SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*01*/new Array("setAttribute",1,"func",-1,0,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*02*/new Array("write",1,"func",-1,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_HTML,SF_KEY_TYPE_FUNC),	
		/*03*/new Array("writeln",0,"func",-1,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_HTML,SF_KEY_TYPE_FUNC), 
		/*04*/new Array("location",1,"url",0,0,SF_TYPE_WINDOW + SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*05*/new Array("src",0,"url",0,0,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*06*/new Array("URL",1,"url",0,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*07*/new Array("href",1,"url",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*08*/new Array("cookie",-1,"cookie",0,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*09*/new Array("innerHTML",-1,"innerhtml",0,0,SF_TYPE_ELEMENT+SF_TYPE_BODY,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*10*/new Array("action",1,"url",0,0,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*11*/new Array("domain",-1,"domain",0,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*12*/new Array("host",-1,"",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*13*/new Array("pathname",-1,"",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*14*/new Array("port",-1,"",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*15*/new Array("protocol",-1,"",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*16*/new Array("hostname",-1,"",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*17*/new Array("navigate",1,"func",1,1,SF_TYPE_WINDOW,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*18*/new Array("assign",1,"func",1,1,SF_TYPE_LOCATION,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*19*/new Array("replace",1,"func",1,1,SF_TYPE_LOCATION,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*20*/new Array("createStyleSheet",2,"func",1,1,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*21*/new Array("behavior",0,"behavior",0,0,"",SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*22*/new Array("addBehavior",0,"func",1,1,"",SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),	
		/*23*/new Array("NavigateAndFind",1,"func",1,0,"",SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC), 
		/*24*/new Array("load",0,"func",1,1,SF_TYPE_ACTIVEX+SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*25*/new Array("insertAdjacentHTML",-1,"func",2,2,SF_TYPE_ELEMENT,SF_TYPE_ARG_HTML,SF_KEY_TYPE_FUNC),
		/*26*/new Array("showModalDialog",1,"func",1,0,SF_TYPE_WINDOW,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*27*/new Array("showModelessDialog",1,"func",1,0,SF_TYPE_WINDOW,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*28*/new Array("background",0,"",0,0, SF_TYPE_ELEMENT+SF_TYPE_STYLE,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*29*/new Array("referrer",1,"",0,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*30*/new Array("data",0,"url",0,0, SF_TYPE_ELEMENT+SF_TYPE_EVENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*31*/new Array("cite",0,"",0,0, SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*32*/new Array("backgroundImage",-1,"backgroundimage",0,0,SF_TYPE_ELEMENT+SF_TYPE_STYLE,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT_RIGHT),
		/*33*/new Array("eval",1,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_JS,SF_KEY_TYPE_FUNC),
		/*34*/new Array("reload",1,"func",1,1,SF_TYPE_LOCATION,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*35*/new Array("attachEvent",1,"func",-1,2,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*36*/new Array("addEventListener",1,"func",-1,2,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*37*/new Array("all",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*38*/new Array("anchors",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*39*/new Array("applets",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*40*/new Array("childNodes",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*41*/new Array("embeds",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*42*/new Array("forms",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*43*/new Array("frames",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*44*/new Array("images",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*45*/new Array("links",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*46*/new Array("namespaces",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*47*/new Array("scripts",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*48*/new Array("styleSheets",1,"",0,0,SF_TYPE_DOCUMENT+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*49*/new Array("body",1,"",0,0,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*50*/new Array("createElement",1,"func",-1,2,SF_TYPE_DOCUMENT,SF_TYPE_ARG_HTML,SF_KEY_TYPE_FUNC),
		/*51*/new Array("detachEvent",1,"func",-1,2,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*52*/new Array("removeEventListener",1,"func",-1,2,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*53*/new Array("Open",1,"func",0,0,SF_TYPE_WINDOW + SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*54*/new Array("Send",1,"func",0,0,SF_TYPE_WINDOW + SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),//for bug 15566
		/*55*/new Array("getAttribute",1,"func",-1,0,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*56*/new Array("outerHTML",-1,"outerhtml",0,0,SF_TYPE_ELEMENT+SF_TYPE_BODY,SF_TYPE_ARG_HTML,SF_KEY_TYPE_LEFT_RIGHT),
		/*57*/new Array("execScript",1,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_JS,SF_KEY_TYPE_FUNC),
		/*58*/new Array("hash",-1,"",0,0,SF_TYPE_LOCATION + SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*59*/new Array("setTimeout",1,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_JS,SF_KEY_TYPE_FUNC),
		/*60*/new Array("setInterval",1,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_JS,SF_KEY_TYPE_FUNC),
		/*61*/new Array("cssText",-1,"",0,0,SF_TYPE_STYLESHEET,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*62*/new Array("textContent",-1,"",0,0,SF_TYPE_STYLESHEET,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*63*/new Array("search",-1,"",0,0,SF_TYPE_LOCATION+SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*64*/new Array("innerText",-1,"",0,0,SF_TYPE_ELEMENT+SF_TYPE_BODY,SF_TYPE_ARG_HTML,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*65*/new Array("value",-1,"",0,0,SF_TYPE_ELEMENT,SF_TYPE_ARG_HTML,SF_KEY_TYPE_RIGHT),
		/*66*/new Array("ActiveXObject",3,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*67*/new Array("Function",-1,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*68*/new Array("getElementById",-1,"func",-1,1,SF_TYPE_ELEMENT+SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*69*/new Array("getElementsByTagName",-1,"func",-1,1,SF_TYPE_ELEMENT+SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*70*/new Array("close",-1,"func",-1,1,SF_TYPE_DOCUMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*71*/new Array("appendChild",-1,"func",-1,1,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*72*/new Array("text",-1,"",0,0,SF_TYPE_ELEMENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*73*/new Array("postMessage",-1,"func",0,0,SF_TYPE_WINDOW,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*74*/new Array("origin",-1,"",0,0,SF_TYPE_EVENT,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*75*/new Array("addImport",2,"func",1,1,SF_TYPE_STYLESHEET,SF_TYPE_ARG_URL,SF_KEY_TYPE_FUNC),
		/*76*/new Array("filter",-1,"",0,0,SF_TYPE_STYLE,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_LEFT),
		/*77*/new Array("setRequestHeader",1,"func",0,0,SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*78*/new Array("onreadystatechange",1,"func",0,0,SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_FUNC),
		/*79*/new Array("readyState",-1,"",0,0,SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT),
		/*80*/new Array("responseText",-1,"",0,0,SF_TYPE_ACTIVEX + SF_TYPE_XMLHTTPREQ,SF_TYPE_ARG_UNKNOW,SF_KEY_TYPE_RIGHT)
		//WARNING:these configureation should keep sync with websvc.conf.
	);
	
	var expStr = "[^a-z_](";
	for(var i=0;i<SF_const_scriptkeys.length;i++)
	{
		expStr += SF_const_scriptkeys[i][0]+"|";
	}
	expStr = expStr.substr(0,expStr.length-1) + ")[^a-z_]";
	SF_JS_KEYWORD_ExpObj = new RegExp(expStr,'gim');
	
	CACHE_FLAG_NONE = 0;
    CACHE_FLAG_DOCUMENT_REPLACE = 1<<0;
	
	SF_FLS_WRITE = 1;
	SF_FLS_WRITELN = SF_FLS_WRITE + 1;
	SF_FLS_INNERHTML = SF_FLS_WRITELN + 1;
	SF_FLS_ALL = SF_FLS_INNERHTML + 1;
	SF_FLS_MAX = SF_FLS_ALL + 1;
	SF_IsPageLoaded = false;
	SF_IsTimeOut = 0;
	
	SF_KEY_TYPE_FUNC = 1;
	SF_KEY_TYPE_RIGHT = 2;
	SF_KEY_TYPE_LEFT = 3;
	SF_KEY_TYPE_LEFT_RIGHT = 4;

	if(typeof(SF_g_flahObjs)=='undefined' || !SF_g_flahObjs) //use svpn_websvc_functions.js as block js may overwrite SF_g_flahObjs;
		SF_g_flahObjs = [];
	
	SF_g_CacheRewrite ={};
	
	var check=function(r){return r.test(navigator.userAgent.toLowerCase());};
	var isStrict=document.compatMode == "CSS1Compat";
	var isOpera=check(/opera/);
	var isChrome=check(/\bchrome\b/);
	var isWebKit=check(/webkit/);
	var isSafari=!isChrome && check(/safari/);
	var isSafari2=isSafari && check(/applewebkit\/4/); 
	var isSafari3=isSafari && check(/version\/3/);
	var isSafari4=isSafari && check(/version\/4/);
	var isIE=!isOpera && check(/msie/);
	var isIE5=isIE && check(/msie 5/);
	var isIE7=isIE && check(/msie 7/);
	var isIE8=isIE && check(/msie 8/);
	var isIE9=isIE && check(/msie 9/);
	var isIE6=isIE && check(/msie 6/);
	var isGecko=!isWebKit && check(/gecko/);
	var isGecko2=isGecko && check(/rv:1\.8/);
	var isGecko3=isGecko && check(/rv:1\.9/);
	var isBorderBox=isIE && !isStrict;
	var isWindows=check(/windows|win32/);
	var isMac=check(/macintosh|mac os x/);
	var isAir=check(/adobeair/);
	var isLinux=check(/linux/);
	var hasXMLHttpRequest = !isIE || isIE8 || isIE9;
	
	var isFake  = (function CheckFakeUrl(url)
	{
		if(typeof(url) != 'string')
			return false;
		var fakeUrlReg = new RegExp(SF_FAKEURL_FLAG,"i");
		return fakeUrlReg.test(url);
	})(SF_FUNC_GetLocation_Script_Compatible());

	SF_Util = {
		isStrict:isStrict,
		isOpera:isOpera,
		isChrome:isChrome,
		isWebKit:isWebKit,
		isSafari:isSafari,
		isSafari2:isSafari2, 
		isSafari3:isSafari3,
		isSafari4:isSafari4,
		isIE5:isIE5,
		isIE:isIE,
		isIE7:isIE7,
		isIE8:isIE8,
		isIE6:isIE6,
		isIE9:isIE9,
		isGecko:isGecko,
		isGecko2:isGecko2,
		isGecko3:isGecko3,
		isWindows:isWindows,
		isMac:isMac,
		isAir:isAir,
		isLinux:isLinux,
		hasXMLHttpRequest:hasXMLHttpRequest,
		isFake:isFake,
		getByteLen:function(input)
		{
			return encodeURIComponent(input).replace(/%[A-Z0-9]{2}/g,"0").length;
		}
	};
	
	
	
	SF_g_CookieStamp = 0;
}

function SF_STRUCT_LOCATION()
{
	this.href="";
	this.host="";
	this.hostname="";
	this.pathname="";
	this.port=80;
	this.protocol="http:";
	this.prototype.toString = function(){return this.href;};	
}

var __SF_g_cookies__ = [];

function SF_func_getcookieheader(pDocObj)
{
	SF_FUNC_RoutineTrace("SF_func_getcookieheader",arguments);
	var s = "";
	//WARN:modify document.domain will not effect the domain of cookie
	//use orgin domain all the time.
	var domain = (pDocObj.SF_g_conofcurloc || SF_g_conofcurloc).svpninfo["host"].replace(/:.*$/,'');
	
	s += ("host=" + domain);
	s += ("&" + "path=" + __SF_g_cookies__["path"]);
	s += ("&" + "secure=" + (__SF_g_cookies__["secure"] ));
	s += "&clienttime=" + new Date().toGMTString();
	return s;
}


function SF_FUNC_GET_SyncCookieStamp()
{
	var cookieName ="websvr_cookie";
	var svrCookieStamp = null;
	var cookies = document.cookie.split("; ")
	
	for (var i=0; i < cookies.length; i++)
	{
		var aCrumb = cookies[i].split("=");
		if (cookieName == aCrumb[0].toLowerCase())
		{ 
			svrCookieStamp = unescape(aCrumb[1]);
			break;
		}
	}
	
	if(isNaN(parseInt(svrCookieStamp)))
		svrCookieStamp = 0;
	else
		svrCookieStamp = parseInt(svrCookieStamp);
	
	if(SF_FUNC_IsUndefined(SF_g_CookieStamp) || isNaN(SF_g_CookieStamp))
		SF_g_CookieStamp = 0;
		
	if(svrCookieStamp > SF_g_CookieStamp)
	{
		SF_g_CookieStamp = svrCookieStamp;
		return true;
	}
	else
		return false;
}



function SF_FUNC_GET_cookie(pDocObj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GET_cookie",arguments);
	if (SF_func_getobjtype(pDocObj) != SF_TYPE_DOCUMENT )
	{
		return pDocObj["cookie"];
	}
	
	if (typeof(__SF_g_cookies__["cookie"]) == "undefined" || SF_FUNC_GET_SyncCookieStamp())//server side will update the stamptime websvc_cookie after cookies changing.
	{
		var cookieHeader = SF_func_getcookieheader(pDocObj);
		__SF_g_cookies__["cookie"] = SF_func_post_http("/ccs/get",cookieHeader);
	}
	return __SF_g_cookies__["cookie"];
}

function SF_FUNC_SET_cookie(pDocObj,pCookieStr)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SET_cookie",arguments);
	if (SF_func_getobjtype(pDocObj) != SF_TYPE_DOCUMENT )
	{
		return pDocObj["cookie"];
	}
	var cookieHeader = SF_func_getcookieheader(pDocObj);
	var sendCookie = "";
	pCookieStr = new String(pCookieStr);
	var cookieArr = pCookieStr.split(";");
	for (var i = 0 ;i < cookieArr.length;i++)
	{
		if(cookieArr[i] == "")
		{
			continue;
		}
		var strC = SF_func_trim(cookieArr[i]); 
		var pos = strC.indexOf("=");
		if(pos < 0)
		{
			pos = strC.length;
		}
		var pKey = SF_func_trim(strC.SF_substring(0,pos));
		var pVal = strC.SF_substr(pos + 1);
		if ( pKey.match(/^path|domain|secure|expires$/i) == null )
		{
			if(i > 0)
			{
		 	 	 sendCookie +=";";
		 	}
		}
		sendCookie += pKey + "=" + pVal + ";"
	}
	cookieHeader += "&cookies=" + SF_func_escape(sendCookie);
	__SF_g_cookies__["cookie"] = SF_func_post_http("/ccs/set",cookieHeader);
	
	SF_FUNC_GET_SyncCookieStamp();
	
	return __SF_g_cookies__["cookie"];
}


function SF_FUNC_GET_domain(pDocObj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GET_domain",arguments);
	if (typeof pDocObj == "undefined")
	{
		return "";
	}
	if (SF_func_getobjtype(pDocObj) != SF_TYPE_DOCUMENT)
	{
		return pDocObj["domain"];
	}
	
	if(!__SF_g_cookies__)
	{
		__SF_g_cookies__ = {};
	}
	return __SF_g_cookies__["domain"] ;
}


function SF_FUNC_SET_domain(pDocObj,pVal)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SET_domain",arguments);
	if (typeof pDocObj == "undefined")
		return pDocObj["domain"] = pVal;
	
	if (SF_func_getobjtype(pDocObj) != SF_TYPE_DOCUMENT)
		return pDocObj["domain"] = pVal;
	
	__SF_g_cookies__["domain"] = pVal ;
	/*try
	{
		//set it's parent or top's domain,or it will be dennied to access it's variables or functions
		if(self.parent)
			self.parent.document.domain = document.domain;
	}catch(e){SF_FUNC_Debug(e.message);}
	try
	{
		if(self.top)
			self.top.document.domain = document.domain;
	}catch(e){SF_FUNC_Debug(e.message);}
	*/
	return  __SF_g_cookies__["domain"];
}

/*** add 20070814 **/


function SF_FUNC_GET_locattr(lochref,attr)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GET_locattr",arguments);
	var parsedhref = SF_func_parsewinlocation(lochref);
	if(parsedhref)
		return parsedhref["location"][attr];
	else
		return null;
}

function SF_FUNC_GET_location()
{
	SF_FUNC_RoutineTrace("SF_FUNC_GET_location",arguments);
    return window.location?window.location:document.location;
}

function SF_FUNC_SET_locattr(pObj,attrName,pVal)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SET_locattr",arguments);
	if( !SF_FUNC_IsUndefined(pObj["SF_WINDOW"]) )
	{
		if(attrName=='hash' && pVal.charAt(0)!='#')
			pVal = '#' + pVal;
		else if(attrName=='search' && pVal.charAt(0)!='?')
			pVal = '?' + pVal;
		else if(attrName=='protocol' && pVal.charAt(pVal.length-1)!=':')
			pVal += ':';
		else if(attrName=='host') 
		{
			var hostName = pVal;
			var port= null;
			if(pVal.indexOf(':') != -1)
			{
				hostName = pVal.split(':')[0];
				port = pVal.split(':')[1];
			}
			
			pObj["hostname"] = hostName;
			
			if(port)
				pObj["port"] = port;
				
		}
		else if(attrName=='hostname')
		{
			if(SF_func_trim(pObj["port"]) != '')
				pObj["host"] = pVal+":"+pObj["port"];
			else
				pObj["host"] = pVal;
		}
		else if(attrName=='port')
		{
			if(pVal.charAt(0)==':')
				pVal = pVal.SF_substr(1);
			pObj["host"] = pObj["hostname"] + ":" + pVal;
		}
		
		pObj[attrName] = pVal;
		pObj["href"] = pObj.protocol+"//"+pObj.host+pObj.pathname+pObj.search + pObj.hash
		return SF_FUNC_transhttpurl(pObj["href"],1);
	}
	else
	{	
		var urlCom = SF_func_parsewinlocation(pObj["href"]);
		urlCom["location"][attrName] = pVal;
		var newUrl = urlCom["location"]["protocol"] + "//" + urlCom["location"]["host"] + urlCom["location"]["pathname"] + pObj.hash + pObj.search;
		return SF_FUNC_transhttpurl(newUrl,1);
	}
}
/*--add function here ----*/



function SF_FUNC_SET_behavior(pObj,pVal)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SET_behavior",arguments);
	if(arguments.length < 2 || typeof pObj == "undefined" 	)
		{
			return;
		}
	if(typeof pVal != "undefined" && pVal != "" && pVal != null )
		{
			var urlPattern = /[^\(]*url\(\s*['"]?([^'"\(\)]*)['"]?[^\)\(]*\)/i;
			var result = urlPattern.exec(pVal);
			var strStart = 0;
			var strEnd = 0;
			var dstVal = "";
			while (result)
			{
				var szUrl = "url(" + SF_FUNC_transhttpurl(result[1],6, window.document.SF_g_conofcurloc) + ")";
				strEnd = result.index;
				var result_lastIndex = strEnd + result[0].length;
				if (szUrl != null && szUrl != result[1])
				{
					dstVal += pVal.SF_substring(strStart,strEnd) + szUrl;
				}
				else
				{
					dstVal += pVal.SF_substring(strStart,result_lastIndex);
				}
				pVal=pVal.SF_substr(result_lastIndex);
			
				result = urlPattern.exec(pVal);
			}
			dstVal += pVal.SF_substr(strStart);
			pObj["behavior"] = dstVal;
		}
		else
		{
			pObj["behavior"] = pVal;
		}
		return pVal;
}


/**********************common****************************************************/
function SF_func_escape(str)
{
	if(typeof(str)!="string")
		return str;
   	var e=/[\x00-\xff]+/;
	var r=null;
	var s="";var start=0;var end=0;var rstr="";
	while ((r = e.exec(str)) != null)
	{
	   end=r.index;
	   rstr+=str.SF_substring(start,end)+escape(r[0]);
	   str=str.SF_substr(end+r[0].length);
	   
    }
    rstr+=str.SF_substr(start);
    return rstr;
}
/***NOT USE**/
function SF_func_rtrim(stringObj)
{
	return stringObj.replace(/^\s+/g,'');
}
/***NOT USE**/
function SF_func_ltrim(stringObj)
{
	return stringObj.replace(/\s+$/g,'');
}
function SF_func_trim(stringObj)
{
	if(typeof(stringObj)!="string")
		return stringObj;
	else if(stringObj == "")
		return "";
	else return stringObj.replace(/^\s+|\s+$/g,'');
}

function SF_func_isundefined(typestr)
{
	if(SF_Util.isIE5)
		return typestr == "unknown";
	else
		return typestr == 'undefined' ;
}

function SF_FUNC_IsUndefined(obj)
{
	var typestr = "undefined";
	if(SF_Util.isIE5)
		typestr = "unknown";
	
	return typeof(obj) == typestr;
}

function SF_func_checkurliftransed(theurl)
{
	SF_FUNC_RoutineTrace("SF_func_checkurliftransed",arguments);
	theurl = new String(theurl);
	theurl = SF_func_trim(theurl);
	var transedpatter = null;
	if(SF_FUNC_IsFake())
		transedpatter = /^(https?:\/\/[^\/]+)?(\/[0-9a-zA-Z]{16,16})?(\/sftwf[a-f0-9]+)?\/safeurl\/web\/\d\/https?\/\d\/.+$/i; 
	else
		transedpatter = /^(https?:\/\/[^\/]+)?(\/[0-9a-zA-Z]{16,16})?(\/sftwf[a-f0-9]+)?\/web\/\d\/https?\/\d\/.+$/i; 
	var result = theurl.match(transedpatter);
	return (result != null);
}

function SF_func_checkurltype(theurl)
{
	SF_FUNC_RoutineTrace("SF_func_checkurltype",arguments);
	var protocolPattern=/^[a-z]{2,8}:(\/\/)?/i;
    if (theurl.charAt(0) =='#' || theurl.charAt(0) == '?' || theurl.SF_substr(0,6) == "about:" || theurl.match(/^\s*(java|vb)script:/i) != null)
	{
		return SF_SYS_UNKNOENPATH;
	}
	var result = theurl.match(protocolPattern);
	if (result) 
	{  
        
		if ((result = theurl.match(/^(https?):\/\/([^\/\?#]+)\/?(.*)/i)) != null)
		{
            
			if (result[2].charAt(0) == ':')
			{
				return SF_SYS_UNKNOENPATH;
			}
			return SF_SYS_FULLPATH;
		}
		else if((result=theurl.match(/(https?:\/)[^\/].*/i)) != null)
		{
			theurl=theurl.SF_substr(result[1].length);
			
			return SF_SYS_ABSPATH;
		}
		else
		{
			return SF_SYS_UNKNOENPATH;
		}
	}
	else
	{
		if (theurl.charAt(0) == '/')
		{
			try{
				if(theurl.charAt(1) == '/')
				{
                    
					return SF_SYS_FULLPATH_1;	
				}
			}
			catch(e){SF_FUNC_Debug(e.message);}
			return SF_SYS_ABSPATH;
		}
		var sexp = /(^(\.\.\/)+)(.*)/;
		var startPos = 0;
		var ret = 0;
		while (theurl.SF_substr(startPos,2) == "./")
		{
			startPos += 2;
		}
		while (theurl.SF_substr(startPos,3) == "../")
		{
			startPos += 3;
			ret ++;
			while(1)
			{
				if(theurl.SF_substr(startPos,2) == "./")
				{
					startPos += 2;
				}else if(theurl.SF_substr(startPos,1) == "/")
				{
					startPos++;
				}
				else{
					break;
				}
			}
		}
		if(ret > 0)
		{
            
			return SF_SYS_RELPATH + ret;
		}
		else
		{
			return SF_SYS_RELPATH;
		}
	}
}


function SF_func_checkfiletype(theUrl,filterType)
{
	SF_FUNC_RoutineTrace("SF_func_checkfiletype",arguments);
	if (filterType == -1)
		return 0;
	
	if(filterType > 0)//specified type
	{
		if(filterType == 3 && theUrl.match(/\.(vbs)$/i) != null)//script include js and vbs
			return 4;
		return filterType;
	}
	theUrl = new String(theUrl);
	theUrl = SF_func_trim(theUrl);
	
	if(theUrl.match(/\.(html|xml|xsl|xslt|aspx|htm|shtm|sthml|mht|mhtml|php|asp|jsp|do)([#\?]?.*)?$/i) != null)
		return 1;
	else if (theUrl.match(/\.(css)$/i) != null)
		return 2;
	else if (theUrl.match(/\.(js)$/i) != null)
		return 3;
	else if(theUrl.match(/\.(vbs)$/i) != null)
		return 4;
	else if (theUrl.match(/\.(jpg|gif|bmp|png|jpeg|doc|exe|rar|zip)$/i) != null)
		return 0;
	else 
		return 1;
}


function SF_func_parsehttpurl(url)
{
	SF_FUNC_RoutineTrace("SF_func_parsehttpurl",arguments);
	var httpUrlPattern = /^((https?):\/\/([^\/\?#]+)?)?\/?([\s\S]*)/i;
	var result = url.match(httpUrlPattern);
	var urlCom = [];
	if(result == null)
	{
		return null;
	}
    
	var fileN = result[4];
	urlCom["protocol"] = result[2];
	urlCom["host"] = result[3];
	urlCom["filepath"] = "";
	urlCom["ext"] = "";
	if(SF_FUNC_IsFake() && urlCom["host"] )
		urlCom["host"] = SF_Encrypt.base64encode(urlCom["host"],true);
	
	if (typeof fileN == "undefined" || fileN == "")
	{
		return urlCom;
	}
	urlCom["filepath"] = fileN;

	return urlCom;
	
}
function SF_func_parsewinlocation(surl)
{
    SF_FUNC_RoutineTrace("SF_func_parsewinlocation",arguments);
	var com_location = SF_FUNC_parsemyurl(surl);
	if (com_location == null)/*the page is not from our proxy server */
	{
		return null;
	}
	var fname = com_location["sFile"];
	var pathend = 0;
	var backpath = "";
	var pathdepth = 0;
	var thehash="";
	var thehref=""
	var thesearch="";
	if (fname != null)
	{
		var lastindex = 1;
		var ret = fname.search(/[#\?]/);
		if(ret >= 0)
		{
			var queryString = fname.SF_substr(ret);
			var matchRet = null;
			if(matchRet = queryString.match(/(\?[^#]+)/))
				thesearch = matchRet[1];
			if(matchRet = queryString.match(/(#.+)/))
				thehash = matchRet[1];
            fname = fname.SF_substring(0,ret);
		}
		ret = 0;
		while (lastindex >= 0)
		{
			pathend = lastindex;
			lastindex = fname.indexOf("/",lastindex);
			if (lastindex >= 0)
			{
                backpath += "../";
				lastindex++;
				ret ++;
			}
		}
		pathdepth = ret;
	}
	if (pathend < 0)
	{
		pathend = 0;
	}
	
    
	var spath = fname.SF_substring(0,pathend);
	if(!spath.match(/\/$/))
	{
		spath += "/";
	}
	if(!spath.match(/^\//))
	{
		spath = "/" + spath;
	}
	var theprotocol = com_location["sProtocol"].toLowerCase();
	var defport = (theprotocol == "https" ? 443 : 80);
	var thehostname = com_location["sHost"];
	var thehost = thehostname;
	var port = "" ;
	if(thehostname.indexOf(":") > 0)
	{
		port = thehostname.SF_substr(thehostname.indexOf(":")+1);
		if(defport === parseInt(port))
			port = "";
		thehostname =  thehostname.SF_substring(0,thehostname.indexOf(":") )
	}
	/*else
	{
		thehost += defport == 80 ? "" : ":" + defport;
	}*/
	if(com_location.sPtype === SF_SYS_REVERSED)
		thehref = surl;
	else
		thehref = theprotocol+"://"+ thehost +  fname + thesearch + thehash;
	
	var com_tmp = {
					"location":
					{
						"host":thehost,
						"pathname":fname,
						"port":port,
						"protocol":theprotocol+":",
						"hostname":thehostname,
						"hash":thehash,
						"search":thesearch,
						"href":thehref
					},
					"svpninfo":
					{
						"pathname":spath,
						"protocol":theprotocol,
						"host":thehost,
						"filter":com_location["sFtype"],
						"urltype":com_location["sPtype"],
						"depth":pathdepth,
						"backpath":backpath
					}
				};

	return com_tmp;
}


function SF_func_geturlroot(url)
{
	SF_FUNC_RoutineTrace("SF_func_geturlroot",arguments);
	var rootindex = url.indexOf("/",8);
	
    if (rootindex < 8)
	{
		return url + "/";
	}
	else
	{
        
		return url.SF_substring(0,rootindex+1);
	}
}

function SF_func_checkfunctionisloaded( sfunction)
{
	SF_FUNC_RoutineTrace("SF_func_checkfunctionisloaded",arguments);
	return 0;
	var fstr = "";
	if (sfunction == null)
	{
		return 1;
	}
	try
	{
		fstr = sfunction.toString();
	}
	catch (e)
	{
		SF_FUNC_Debug(e.message);
		return 0;
	}
	return fstr.match(/^.*SF_FUNC/) != null;
}


function SF_func_checkfunctionstr( sfunction,  functionstring)
{
	SF_FUNC_RoutineTrace("SF_func_checkfunctionstr",arguments);
	/* modify by zhk,2007-03-16 */
	if (SF_func_checkfunctionisloaded(sfunction) == 0)
	{
		return 0;
	}

	if (sfunction.tostring ==functionstring)
	{
		return 1;
	}
	return 0;
}



function SF_func_createxmlhttp(clsName)
{
	SF_FUNC_RoutineTrace("SF_func_createxmlhttp",arguments);
	
	try
	{
		if (SF_Util.hasXMLHttpRequest && typeof XMLHttpRequest != 'undefined') 
			return new window.XMLHttpRequest();
	}catch(e){}
	
	if(clsName)//if there there specified the clsName,then use the value to construct the ajax object;
		try{return new ActiveXObject(clsName);}catch(e){}
	
	var msxmls = [ 'MSXML2.XMLHttp.6.0','MSXML2.XMLHttp.5.0','MSXML2.XMLHttp.4.0','MSXML2.XMLHttp.3.0', 'MSXML2.XMLHttp', 'Microsoft.XMLHttp'];
	if(SF_Util.isIE5)
		msxmls = ['Microsoft.XMLHttp','MSXML3.XMLHttp', 'MSXML2.XMLHttp' ];
	
	for (var i=0; i < msxmls.length; i++) 
	{
		try 
		{
			return new ActiveXObject(msxmls[i])
		} catch (e) { SF_FUNC_Debug(e.message); }
	}
	return null;
	
}

function SF_func_post_http(url,body)
{
	SF_FUNC_RoutineTrace("SF_func_post_http",arguments);
	try{
		if(/javascript:/.test(window.location.href)
			|| /about:/i.test(window.location.href)
		)
		{
			var parents = [parent,opener,top];
			for(var i=0;i<parents.length;i++)
			{
				var winobj = parents[i];
				if(winobj.window && winobj.window != window && winobj.window.SF_func_post_http)
				{
					//return winobj.window.SF_func_post_http.apply(winobj,arguments);
					return winobj.window.SF_func_post_http(url,body);
				}
			}
		}
	}catch(e){};
	
	var xmlhttp = null;
	xmlhttp = SF_func_createxmlhttp();
	if(xmlhttp == null)
	{
		return "";
	}
	if(body)
		xmlhttp.open("POST", url , false);	
	else
		xmlhttp.open("GET", url , false);
		
	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	try{
		xmlhttp.setRequestHeader('X-SVPN-REFURL', SF_FUNC_GET_location().href);
	}catch(e){}
	
	try
	{
		if (body)
		{
			xmlhttp.send(body);
		}
		else
		{
			xmlhttp.send(null);
		}
	}
	catch (e)
	{
		SF_FUNC_Debug(e.message);
		return "";
	}

	return SF_func_trim(xmlhttp.responseText ? xmlhttp.responseText : "");
}

/*********************END*******************************************************/


function SF_func_checknodeattrfiltertype(node,att)
{
	SF_FUNC_RoutineTrace("SF_func_checknodeattrfiltertype",arguments);
	var filterType = 0;
	node = node.toLowerCase();
	att = att.toLowerCase();

	if (att == "href" && (node == "a" || node == "area"))
	{
		return 1;
	}
	/* add by zhk,2007-03-16 */
	if (att == "href" && node == "link")
	{
		return 2;
	}

	if (att == "src")
	{
		if (node == "iframe" || node == "frame")
		{
			return 1;
		}
		else if (node == "script")
		{
			return 3;
		}
		else
		{
			return 0;
		}
	}
	else if (att == "action" && node == "form")
	{
		return 1;
	}
	return 0;
}

/******************** TRANSLATE URL *************************/

function SF_FUNC_transhttpurl(oldurl,filterType,refurl)
{
	SF_FUNC_RoutineTrace("SF_FUNC_transhttpurl",arguments);
	if (oldurl == null || oldurl == "")
	{
		return oldurl;
	}
	
	if(typeof(oldurl) != "string")
		return oldurl;
		
	//oldurl = oldurl.toString();

	oldurl = SF_func_trim(oldurl);
  
	if (SF_func_checkurliftransed(oldurl))
	{
        return oldurl;
	}
    
	var path_type =  SF_func_checkurltype(oldurl);
    if (path_type <= 0 )
	{
        
		return oldurl;
	}
    
	if(path_type == SF_SYS_FULLPATH_1)
	{
		oldurl = refurl["svpninfo"]["protocol"]+":" + oldurl;
		path_type = SF_SYS_FULLPATH;
		
	}
	else
	{	var result=oldurl.match(/(https?:\/)[^\/].*/i);
		if(result!= null)
		{
			oldurl=oldurl.SF_substr(result[1].length);
		}
	}
	if (filterType == null)
	{
		filterType = 1;
	}

	filterType = SF_func_checkfiletype(oldurl, filterType);
	if (path_type >= SF_SYS_RELPATH)
	{
        
		var tmp = path_type - SF_SYS_RELPATH - refurl.svpninfo["depth"];
	
		if (tmp < 0)
		{
			tmp = 0;
		}
		var startPos = 0;
		while (oldurl.SF_substr(startPos,2) == "./" && oldurl.SF_substr(startPos,3) != "./?")
		{
			startPos += 2;
		}
		while (tmp > 0 && oldurl.SF_substr(startPos,3) == "../")
		{
			tmp --;
			startPos += 3;
			while(1)
			{
				if(oldurl.SF_substr(startPos,2) == "./" && oldurl.SF_substr(startPos,3) != "./?")
				{
					startPos += 2;
				}else if(oldurl.SF_substr(startPos,1) == "/")
				{
					startPos++;
				}
				else{
					break;
				}
			}
		}
		oldurl = oldurl.SF_substr( startPos);
		if(filterType == 1 && refurl["location"]["pathname"] != "")
			return oldurl;
	}
    
	var parsed_url = SF_func_parsehttpurl(oldurl);
	if (parsed_url == null)
	{
		return oldurl;
	}

	if (parsed_url.path == "undefined")
	{
		parsed_url.path = "";
	}
	var transed_url = oldurl;
	
	
	var hostSvpnInfo,hostLocation;
	if(refurl)
	{
		if(SF_FUNC_IsFake())
		{
			hostSvpnInfo = SF_Encrypt.base64encode(refurl["svpninfo"]["host"],true);
			hostLocation = SF_Encrypt.base64encode(refurl["location"]["host"],true);
		}
		else
		{
			hostSvpnInfo = refurl["svpninfo"]["host"];
			hostLocation = refurl["location"]["host"];
		}
	}
	
	
	if(SF_FUNC_IsFake() && SF_SYS_WEBURLHEAD.indexOf(SF_FAKEURL_FLAG) == -1)//
	{
		SF_SYS_WEBURLHEAD = SF_FAKEURL_FLAG + SF_SYS_WEBURLHEAD;
	}
	
	
	switch (path_type)
	{
		case SF_SYS_UNKNOENPATH:
		case SF_SYS_FTPPATH:
		{
			break;
		}
		case SF_SYS_FULLPATH:
		{/*URL_DIM*/
			transed_url = SF_g_svpnurl + SF_SYS_WEBURLHEAD  + filterType + "/" + parsed_url["protocol"] + "/0/" + parsed_url["host"] + "/" + parsed_url["filepath"];
			break;
		}
		case SF_SYS_ABSPATH:
		{/*URL_DIM*/
			transed_url = SF_g_svpnurl + SF_SYS_WEBURLHEAD  + filterType + "/" + refurl["svpninfo"]["protocol"] + "/1/" + hostSvpnInfo + "/" + parsed_url["filepath"];
			break;
		}
		default:
		{/*URL_DIM*/
			transed_url = SF_SYS_WEBURLHEAD + filterType +  "/" + refurl["svpninfo"]["protocol"] + "/2/" + hostLocation +  refurl["svpninfo"]["pathname"]  + parsed_url["filepath"];
		}
	}
	return transed_url;

}


function SF_FUNC_parsemyurl(ourl)
{
	SF_FUNC_RoutineTrace("SF_FUNC_parsemyurl",arguments);
	if (ourl == null || ourl == "")
	{
		return null;
	}
	ourl = new String(ourl);
	var c = {};
	var ret = ourl.match(/^(javascript|vbscript|about):(.*)$/);
	if(ret)
	{
		c.sHost = ret[2];
		c.sProtocol = ret[1] +":";
		c.sFile = "";
		c.sPtype = SF_SYS_REVERSED;
		c.sFtype= 0;
		return c;
	}
	
	if (!SF_func_checkurliftransed(ourl))
	{
		return null;
	}
	

	/*URL_DIM*/
	var myurlPatter = null;
	if(SF_FUNC_IsFake())
		myurlPatter = /^(https?:\/\/[^\/]+)?(\/[0-9a-zA-Z]{16,16})?(\/sftwf[a-f0-9]+)?\/safeurl\/web\/(\d)\/(https?)\/(\d)\/([^\/]+)(\/?.*)$/i; 
	else
		myurlPatter = /^(https?:\/\/[^\/]+)?(\/[0-9a-zA-Z]{16,16})?(\/sftwf[a-f0-9]+)?\/web\/(\d)\/(https?)\/(\d)\/([^\/]+)(\/?.*)$/i; 
	var result = ourl.match(myurlPatter);
	if(result == null)
	{
		return null;
	}
	
	if(SF_FUNC_IsFake() && result[7])
		result[7] = SF_Encrypt.base64decode(result[7],true);
	
	c={ 
		sFtype 		: result[4],
		sProtocol	: result[5],
		sPtype		: result[6],
		sHost		: result[7],
		sFile		: result[8]
		};
	return c;
}

function SF_FUNC_revertmyurl(ourl,reloc,isSysUrl)
{
	SF_FUNC_RoutineTrace("SF_FUNC_revertmyurl",arguments);
	var com_url = SF_FUNC_parsemyurl(ourl);

	if (com_url == null || com_url.sPtype == SF_SYS_REVERSED)
	{
		return ourl;
	}
	ourl = new String(ourl);
	
	if(com_url["sFile"].charAt(0) == '/')
	{
		com_url["sFile"] = com_url["sFile"].SF_substring(1);
	}
	
	var url_type = com_url["sPtype"] * 1; 
	if(isSysUrl && isSysUrl === true)
		url_type = SF_SYS_URL_COM;
	switch (url_type)
	{
		case SF_SYS_URL_COM:
		{
			return com_url["sProtocol"] + "://" + com_url["sHost"] + "/" + com_url["sFile"];
			break;
		}
		case SF_SYS_URL_ALL:
		{
			return "/" + com_url["sFile"];
			break;
		}
		case SF_SYS_URL_SUB:
		{
			var filePath = SF_FUNC_parsemyurl(SF_FUNC_GetLocation_Script_Compatible())["sFile"].replace(/[?#].*$/g,'');
			var curDictory = filePath.replace(/\/[^\/]+$/,'/');
			var tmpFile = "/" + com_url["sFile"];
			if(tmpFile.indexOf(curDictory)==0)
				tmpFile = tmpFile.SF_substr(curDictory.length);
			
			if(!reloc)
				return tmpFile;
			
			if(tmpFile.indexOf(reloc.location["pathname"]) == 0)
			{
				tmpFile = tmpFile.SF_substr(reloc.location["pathname"].length);
			}
			return  tmpFile;
			break;
		}
	
		default:
		{
			return ourl;
		}
	}

}



function SF_func_getwinurl(winobj)
{
	SF_FUNC_RoutineTrace("SF_func_getwinurl",arguments);
	if (winobj == null)
	{
		return "";
	}
	var sPattern = null;
	if(SF_FUNC_IsFake())
		myurlPatter = /\/safeurl(\/web\/)([^\?#]*)/i; 
	else
		myurlPatter = /(\/web\/)([^\?#]*)/i;  
		
	var locationurl =  SF_FUNC_GetLocation_Script_Compatible();

	if(!locationurl.test(myurlPatter))
		return winobj.location["href"];
	else
		return SF_FUNC_revertmyurl(winobj.location["href"]);
}


function SF_func_getbrowertype()
{
	SF_FUNC_RoutineTrace("SF_func_getbrowertype",arguments);
	if (typeof navigator != "undefined" && typeof navigator.appName == "string")
	{
		if (/Microsoft/i.exec(navigator.appName))
		{
			return "IE";
		}
		else if (/Netscape/i.exec(navigator.appName))
		{
			return "NS";
		}
	}

	if (	typeof top.document.implementation != "undefined" 	&&
			typeof top.document.doctype != "undefined" 		&&
			typeof top.document.height == "number" 		  	&&
			typeof top.document.width == "number" )
	{
		return "NS";
	}

	if (	typeof top.document.activeElement != "undefined" 	&&
			typeof top.document.charset == "string" 		&&
			typeof top.document.frames != "undefined" 		&&
			typeof top.ActiveXObject != "undefined" )
	{
		return "IE";
	}

	return "other";
}


function SF_func_isurlcontent(obj)
{
	SF_FUNC_RoutineTrace("SF_func_isurlcontent",arguments);
	return ( typeof obj == "string" || (SF_func_getobjtype(obj) == SF_TYPE_LOCATION) );
}





function SF_FUNC_Eval_Function(Obj,Func,ArgArr)
{
	SF_FUNC_RoutineTrace("SF_FUNC_Eval_Function",arguments);
	if(arguments.length != 3)
		return;
	//sometimes the usr's code will modify the window.open.
	if(SF_func_getobjtype(Obj)==SF_TYPE_WINDOW
		&& Func == "open"
		&& Obj["SF_"+Func]
		)
		Func = "SF_"+Func;
	
	var Alen = ArgArr.length;
    try
	{
		switch(Alen)
		{
			case 0:
			return Obj[Func]();
			case 1:
			return Obj[Func](ArgArr[0]);
			case 2:
			return Obj[Func](ArgArr[0],ArgArr[1]);
			case 3:
			return Obj[Func](ArgArr[0],ArgArr[1],ArgArr[2]);
			case 4:
			return Obj[Func](ArgArr[0],ArgArr[1],ArgArr[2],ArgArr[3]);
			case 5:
			return Obj[Func](ArgArr[0],ArgArr[1],ArgArr[2],ArgArr[3],ArgArr[4]);
			default:
			;
		}
		var szArgs = "";
		for(var i = 0;i < Alen - 1;i++)
		{
			szArgs += "ArgArr[" + i.toString() + "]" + ",";
		}
		szArgs += "ArgArr[" + (Alen - 1) + "]";
		var JsCode = "Obj." + Func + "(" + szArgs + ")";
		return eval(JsCode);
	}
	catch(e){
		SF_FUNC_Debug(e.message);
		throw e;
	}
	
}


function SF_FUNC_Eval_NewFunction(Obj,Func,hasNewOp,ArgArr)
{
	SF_FUNC_RoutineTrace("SF_FUNC_Eval_NewFunction",arguments);
	if(arguments.length != 4)
		return;
	
	var Alen = ArgArr.length;
    
    try
	{
		if(hasNewOp)
		{
			switch(Alen)
			{
				case 0:
				return new Obj[Func]();
				case 1:
				return new Obj[Func](ArgArr[0]);
				case 2:
				return new Obj[Func](ArgArr[0],ArgArr[1]);
				case 3:
				return new Obj[Func](ArgArr[0],ArgArr[1],ArgArr[2]);
				case 4:
				return new Obj[Func](ArgArr[0],ArgArr[1],ArgArr[2],ArgArr[3]);
				case 5:
				return new Obj[Func](ArgArr[0],ArgArr[1],ArgArr[2],ArgArr[3],ArgArr[4]);
				default:
				;
			}
			var szArgs = "";
			for(var i = 0;i < Alen - 1;i++)
			{
				szArgs += "ArgArr[" + i.toString() + "]" + ",";
			}
			szArgs += "ArgArr[" + (Alen - 1) + "]";
			var JsCode = "new Obj." + Func + "(" + szArgs + ")";
			return eval(JsCode);
		}
		else
			return SF_FUNC_Eval_Function(Obj,Func,ArgArr);
	}
	catch(e){
		SF_FUNC_Debug(e.message);
		throw e;
	}
	
}

/********************* SF_FUNC_PARSEHTML **************************/



function SF_FUNC_PARSEHTML(pHtml,refurl,flag)
{
	SF_FUNC_RoutineTrace("SF_FUNC_PARSEHTML",arguments);
	if ( arguments.length < 2 ||  pHtml == "")
	{
		return pHtml;
	}
	pHtml = new String(pHtml);
	var srcHtmlStr = pHtml.toString();

    if(flag == 2)
	{
		return SF_FUNC_Svr_Rewrite(srcHtmlStr,SF_REWRITE_TYPE_HTML, CACHE_FLAG_POPUP_REPLACE);
	}
	return SF_FUNC_Svr_Rewrite(srcHtmlStr,SF_REWRITE_TYPE_HTML);
}


/** 
    *Core Fun for rewrite and restore HTML.
	*pHtml[String]:object call the [GS]etAttribute.
	*refurl[String]:arguments of the [GS]etAttribute.
	*flag[integer 0|1 option]:flag specified the process the html code.rewrite or restore html code.1 restore the html code,others rewrite
    *retvalue:processed html code;
**/
function SF_FUNC_HtmlParser(pHtml,refurl,flag)
{
	SF_FUNC_RoutineTrace("SF_FUNC_HtmlParser",arguments);
	if ( arguments.length < 2 ||  pHtml == "")
	{
		return pHtml;
	}
	pHtml = new String(pHtml);
	var srcHtmlStr = pHtml.toString();
	var htmlAttExp =
		/(<([a-z]+)\b[^>]*\b(href|src|dynsrc|lowsrc|action|background|codebase|cite|data|profile|archive)\b=\s*)("([^"]*)"|'([^']*)'|([^\s>]*)([\s>]))/i;
	var nothinexp =/"/;

	if (srcHtmlStr == null || srcHtmlStr == "")
	{
		return srcHtmlStr;
	}
	if (refurl == null || refurl == "")
	{
		return pHtml;
		//refurl = window.document.SF_g_conofcurloc;
	}
	if (flag == null)
	{
		flag = 0;
	}
	//var pattern = new RegExp(htmlAttExp);
	var result = null;
	var szNode = "",szAtt = "",szValue = "";
	var dstHtmlStr = "";
	var strStart = 0;
	var strEnd = 0;
//	result = pattern.exec(srcHtmlStr);
    result=htmlAttExp.exec(srcHtmlStr);
	while (result)
	{
		var tmpStr = result[4];
		szNode = result[2];
		var srcstr=result[0];
		var dststr="";
		{
			var htmlAttExp_e =
			/(\b(href|src|dynsrc|lowsrc|action|background|codebase|cite|data|profile|archive)\b=\s*)("([^"]*)"|'([^']*)'|([^\s>]*))/i;
			var nothinexp_e =/"/;
			var sresult=htmlAttExp_e.exec(srcstr);
			while(sresult)
			{
				var tmpStr = sresult[3];
				szAtt = sresult[2];
				szValue = sresult[4] || sresult[5] || sresult[6];
			
				var filterType = SF_func_checknodeattrfiltertype(szNode, szAtt);
				var newUrl = "";
				if (flag == 0)
				{
					newUrl = SF_FUNC_transhttpurl(szValue,filterType,refurl);
				}
				else
				{
					newUrl = SF_FUNC_revertmyurl(szValue,refurl);
				}

				var i=sresult.index;
				var l = i + sresult[0].length;
				if (newUrl != null && newUrl != szValue)
				{
					dststr += srcstr.SF_substring(0,i) + sresult[1];
					dststr += tmpStr.replace(szValue,newUrl);
				}
				else
				{
					dststr += srcstr.SF_substring(0,l);
				}
				srcstr=srcstr.SF_substr(l);
				sresult=htmlAttExp_e.exec(srcstr);
			}
			dststr+=srcstr;
			
		}
		strEnd = result.index;
		var result_lastIndex = strEnd + result[0].length;
		dstHtmlStr += srcHtmlStr.SF_substring(0,strEnd)+dststr;

		srcHtmlStr=srcHtmlStr.SF_substr(result_lastIndex);
		//result = pattern.exec(srcHtmlStr);
		result=htmlAttExp.exec(srcHtmlStr);
	}

	dstHtmlStr += srcHtmlStr.SF_substr(strStart);

	return SF_FUNC_PARSEPARAMHTML(dstHtmlStr,refurl,flag);
}


function SF_FUNC_PARSEPARAMHTML(pHtml,refurl,flag)
{
	SF_FUNC_RoutineTrace("SF_FUNC_PARSEPARAMHTML",arguments);
	var srcHtmlStr = pHtml.toString();
	var htmlAttExp = /(<param\s+name\s*=\s*("([^"]*)"|'([^']*)'|([^\s>]*))\s+value\s*=\s*)("([^"]*)"|'([^']*)'|([^\s>]*)[\s>])/i;
	var nothinexp =/"/;

	if(srcHtmlStr == null || srcHtmlStr == "")
	{
		  return srcHtmlStr;
	}
	if(refurl == null)
	{
		return pHtml;
	}
	
	var result;
	var szAtt,szValue;
	var dstHtmlStr = "";
	var strStart = 0;
	var strEnd = 0;
	
	result=htmlAttExp.exec(srcHtmlStr);
	while(result)
	{
		var tmpStr = result[6];
		
		szAtt = result[3] || result[4] || result[5];
		
		szValue = result[7] || result[8] || result[9];
		
		strEnd = result.index;
		var result_lastIndex = strEnd + result[0].length;
		
		if(szAtt.match(/^\s*(movie)|(file)|(path)|(src)|(filename)\s*$/i) != null)
		{
			var newUrl = "";
			
			if(flag == 0)
			{
				newUrl = SF_FUNC_transhttpurl(szValue,-1,refurl);
			}
			else
			{
				newUrl = SF_FUNC_revertmyurl(szValue);
			}
			
			if(newUrl != null && newUrl != szValue)
			{
				dstHtmlStr += srcHtmlStr.SF_substring(strStart,strEnd) + result[1];
				dstHtmlStr += tmpStr.replace(szValue,newUrl);
			}
			else
			{
				dstHtmlStr += srcHtmlStr.SF_substring(strStart,result_lastIndex);
			}
		}
		else
		{
			dstHtmlStr += srcHtmlStr.SF_substring(strStart,result_lastIndex);
		}
		
		srcHtmlStr=srcHtmlStr.SF_substr(result_lastIndex);
		
		result=htmlAttExp.exec(srcHtmlStr);
	}
	dstHtmlStr += srcHtmlStr.SF_substr(strStart);
	srcHtmlStr = dstHtmlStr;
	htmlAttExp = /(<param\s+value\s*=\s*)("([^"]*)"|'([^']*)'|([^\s>]*))(\s+name\s*=\s*("([^"]*)"|'([^']*)'|([^\s>]*))[\s>])/i;
	var nothinexp =/"/;

	dstHtmlStr = "";
	strStart = 0;
	strEnd = 0;

    result=htmlAttExp.exec(srcHtmlStr);
	while (result)
	{
		var tmpStr = result[2];
		szAtt = result[8] || result[9] || result[10];
		szValue = result[3] || result[4] || result[5];
		strEnd = result.index;
		var result_lastIndex = strEnd + result[0].length;
		if (szAtt.match(/^\s*(movie)|(file)|(path)|(src)|(filename)\s*$/i) != null)
		{
			var newUrl = "";

			if (flag == 0)
			{
				newUrl = SF_FUNC_transhttpurl(szValue,-1,refurl);
			}
			else
			{
				newUrl = SF_FUNC_revertmyurl(szValue);
			}
			if (newUrl != null && newUrl != szValue)
			{
				dstHtmlStr += srcHtmlStr.SF_substring(strStart,strEnd) + result[1];
				dstHtmlStr += tmpStr.replace(szValue,newUrl);
				dstHtmlStr +=  result[6];
			}
			else
			{
				dstHtmlStr += srcHtmlStr.SF_substring(strStart,result_lastIndex);
			}
		}
		else
		{
			dstHtmlStr += srcHtmlStr.SF_substring(strStart,result_lastIndex);
		}
		
		srcHtmlStr=srcHtmlStr.SF_substr(result_lastIndex);
		
		result=htmlAttExp.exec(srcHtmlStr);
	}
	dstHtmlStr += srcHtmlStr.SF_substr(strStart);

	return dstHtmlStr;
}
/***************************************************************************/


window.document.__sf_has_this__ = true;
window.document.__sf_location__ = null;
window.document.SF_g_doctext = "";
window.document.__sf_locations__ =[];


/****************************functions ******************************************/

function SF_FUNC_SET_element_html(pObj,pAttrName,pVal,flag)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SET_element_html",arguments);
	var refurl = window.document.SF_g_conofcurloc;
	if(typeof(pObj.ownerDocument) !=  "undefined" && typeof(pObj.ownerDocument.SF_g_conofcurloc) != "undefined")
		refurl = pObj.ownerDocument.SF_g_conofcurloc;	
	
	/*if popup's object  opopup.body["pAttrName"]=*/
	var objType = SF_func_getobjtype(pObj);
	if( objType== SF_TYPE_BODY)
	{
		if(SF_Util.isIE)
		{
			var myownerDocument = pObj.ownerDocument;
			if(myownerDocument.parentWindow != window)
			{
				pObj[pAttrName] = SF_FUNC_PARSEHTML(pVal,refurl,2);
				return pVal;
			}
        }
	}
	else if(objType == SF_TYPE_ELEMENT && pObj.tagName.toLowerCase()=="textarea" )
	{
		return pObj[pAttrName] = pVal;
	}
	
	pObj[pAttrName] = SF_FUNC_PARSEHTML(pVal,refurl,0);
	return pVal;
}

function SF_FUNC_GET_element_html(pObj,pAttrName)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GET_element_html",arguments);
	var refurl = window.document.SF_g_conofcurloc;
	if(typeof(pObj.ownerDocument) !=  "undefined" && typeof(pObj.ownerDocument.SF_g_conofcurloc) != "undefined")
		refurl = pObj.ownerDocument.SF_g_conofcurloc;	
	
	var htmlStr = pObj[pAttrName];
	//var regexp=/(<(\w+)[^<>]*sf_script=(['"])1\3[^>]*>[\s\S]*<\/\2>)/ig;
	var regexp=/<(\w+)\s*[^>]+?sf_script=['|"]1['|"][^>]*?>[\s\S]*?<\/\1>/ig;
	
	var strFilter=htmlStr.replace(regexp,'');
	//delete the inner script inserted by websvc;
	strFilter = strFilter.replace(/;?if\(typeof\(SF_FUNC_cache_flush\)=='function'\)SF_FUNC_cache_flush\(\);\s*<\/script>/gi,"<\/script>");
	strFilter = strFilter.replace(/;?if\sisobject\(SF_FUNC_cache_flush\)\sthen\sSF_FUNC_cache_flush\(\)\s*<\/script>/gi,"<\/script>");
	
	return SF_FUNC_HtmlParser(strFilter,refurl,1);
}

function SF_FUNC_CALL_insertAdjacentHTML(pElem,sPos,html)
{
	SF_FUNC_RoutineTrace("SF_FUNC_CALL_insertAdjacentHTML",arguments);
	var szObjectType = SF_func_getobjtype(pElem);
	if(szObjectType != SF_TYPE_ELEMENT && szObjectType != SF_TYPE_BODY)
	{
		return pElem["insertAdjacentHTML"](sPos,html);
	}
	var l = document.SF_g_conofcurloc;

	if(typeof(pElem.document) != "undefined" && typeof(pElem.document.SF_g_conofcurloc) != "undefined")
	{
		l = pElem.document.SF_g_conofcurloc;
	}
	var shtml = SF_FUNC_PARSEHTML(html,l);

	return pElem["insertAdjacentHTML"](sPos,shtml);
	
}


function SF_func_getobjtype_without_catch(obj)
{
	SF_FUNC_RoutineTrace("SF_func_getobjtype_without_catch",arguments);
	if( obj.ownerDocument != null
		   && typeof obj.ownerDocument != "undefined"
			&& typeof obj.ownerDocument != "unknown"
			&& obj.ownerDocument.body == obj)
	{
		return SF_TYPE_BODY;
	}
	
	if(!SF_FUNC_IsUndefined(obj["origin"])
		&&!SF_FUNC_IsUndefined(obj["data"]) 
		&&! SF_FUNC_IsUndefined(obj["source"])
		//&&! SF_FUNC_IsUndefined(obj["ports"]) Not included in IE.
		//&&! SF_FUNC_IsUndefined(obj["lastEventId"])
	)
	{
		return SF_TYPE_EVENT;
	}


	if (typeof obj.tagName == "string")
	{
		return SF_TYPE_ELEMENT;
	}
	
	if (	typeof obj["domain"] != "undefined" &&
			//typeof obj["cookie"] != "undefined" &&	//safari access obj.cookie£¬Error: SECURITY_ERR: DOM Exception 18: An attempt was made to break through the security 
			typeof obj.anchors != "undefined" &&
			typeof obj.applets != "undefined" &&
			typeof obj.forms != "undefined" )
	{
		return SF_TYPE_DOCUMENT;
	}
	
	if (	(typeof obj.defaultStatus != "undefined")&&//pad's window has no defaultStatus
			typeof obj.document != "undefined" &&
			typeof obj["location"] != "undefined" &&
			typeof obj.history != "undefined" &&
			typeof obj.top != "undefined" )
	{
		return SF_TYPE_WINDOW;
	}
	
	if (	typeof obj.hash != "undefined" &&
			typeof obj["href"] != "undefined" &&
			typeof obj["pathname"] != "undefined" &&
			typeof obj["protocol"] != "undefined" )
	{
		return SF_TYPE_LOCATION;
	}
	
	if (	typeof obj.media != "undefined" &&
			typeof obj.parentStyleSheet != "undefined" &&
			typeof obj.title != "undefined" &&
			typeof obj.type != "undefined" &&
			typeof obj.disabled != "undefined" )
	{
		return SF_TYPE_STYLESHEET;
	}
	
	if (typeof(XMLDocument) != "undefined" && obj instanceof XMLDocument)//There is no public standard that applies to this property.  
		return SF_TYPE_XMLDOCUMENT;
	else if (SF_Util.hasXMLHttpRequest && 
			 !SF_Util.isIE7 && typeof(XMLHttpRequest) != "undefined" && /*obj instanceof XMLHttpReques in IE will cause a exception*/
			 (obj instanceof XMLHttpRequest)
			)
	{
		return SF_TYPE_XMLHTTPREQ;
	}
	
	if(typeof obj.cssText != "undefined" 
		&& typeof obj.listStylePosition != "undefined" 
	)
	{
		return SF_TYPE_STYLE;
	}
		
	if (SF_Util.isIE && obj instanceof ActiveXObject)
	{
		return SF_TYPE_ACTIVEX;
	}
	if(SF_Util.isIE9 && obj.toString==undefined&&obj.nodeName=="#document")
	{
		//bug22571
		return SF_TYPE_ACTIVEX;
	}
	
	if (SF_Util.isIE 
		&& Object.prototype.hasOwnProperty.call(obj, 'send')
		&& Object.prototype.hasOwnProperty.call(obj, 'open')
		&& Object.prototype.hasOwnProperty.call(obj, 'abort')
		&& Object.prototype.hasOwnProperty.call(obj, 'setRequestHeader'))
	{
		return SF_TYPE_XMLHTTPREQ;
	}
	
	if(!SF_FUNC_IsUndefined(obj["send"])
			&&!SF_FUNC_IsUndefined(obj["open"])
			&&!SF_FUNC_IsUndefined(obj["abort"])
			&&!SF_FUNC_IsUndefined(obj["setRequestHeader"])
		)
	{
		return SF_TYPE_XMLHTTPREQ;
	}
	
	
	if(!SF_FUNC_IsUndefined(obj["splitText"]) 
		&&! SF_FUNC_IsUndefined(obj["replaceData"])
		&&! SF_FUNC_IsUndefined(obj["substringData"])
		&&! SF_FUNC_IsUndefined(obj["length"])
	)
	{
		return SF_TYPE_TEXTNODE;
	}
	
	return SF_TYPE_UNKNOWN;
}

function SF_func_getobjtype(obj)
{
	SF_FUNC_RoutineTrace("SF_func_getobjtype",arguments);
	if ((typeof obj != "object") || (obj == null))
	{
		return SF_TYPE_NOTOBJ;
	}
	
	try{
		if (typeof obj._SVPN_TYPE != "undefined" && typeof obj._SVPN_TYPE != "unknown"){
			return obj._SVPN_TYPE;
		}
	}catch(e){}
	
	var ret = SF_TYPE_UNKNOWN;
	try{
			ret = SF_func_getobjtype_without_catch(obj);
			return ret;
	}catch(e){
		//iframe.src='javascript:;' will throw a exception above.
		try{
			if(typeof obj['location'] != 'undefined')
			{
				if(typeof obj['window'] != 'undefined')
					ret = SF_TYPE_WINDOW;
			}
		}catch(ex){}
	}
	
	if(ret != SF_TYPE_UNKNOWN)
	{
		try{obj._SVPN_TYPE = ret;}catch(e){};
		return ret;
	}
	else
	{
		return SF_func_getobjtype_without_catch(obj);
	}
	//return SF_TYPE_UNKNOWN;	//We could not identify the object!!!
}






function SF_func_settypeofthiswindow(thisWindow)
{
	SF_FUNC_RoutineTrace("SF_func_settypeofthiswindow",arguments);
	if (thisWindow)
	{
		thisWindow._SVPN_TYPE=SF_TYPE_WINDOW;
		thisWindow.document._SVPNTYPE=SF_TYPE_DOCUMENT;
		thisWindow.location._SVPNTYPE=SF_TYPE_LOCATION;

		var iIndex;
		for (iIndex = 0; iIndex < thisWindow.document.styleSheets.length; ++iIndex)
		{
			thisWindow.document.styleSheets[iIndex]._SVPN_TYPE = SF_TYPE_STYLESHEET;
		}
	}
}



function SF_FUNC_SETATT(OBJ,ATTR,STR)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SETATT",arguments);
	STR = SF_FUNC_UnEscape(STR);//process string escaped by xsl
	if (OBJ == null || ATTR == null ||  ATTR < 0 || ATTR >= SF_const_scriptkeys.length )
	{
		return STR;
	}
	try{
		if(typeof(OBJ) == "number" && OBJ == 0)
		{
			OBJ = window;
		}
	}catch(e){SF_FUNC_Debug(e.message);}
	if(ATTR < 0 || ATTR >= SF_const_scriptkeys.length )
	{
		return STR;
	}
	
	if(STR !== null && STR !== undefined && STR.constructor === String)
		STR = STR.toString();
	
	/*Process Custom ActiveX Applet SWF attribute rewrite*/
    //if it's a Custom object,ATTR should be a string but not a integer.
    if(typeof(ATTR) == "string")
    {
		var reWriteInfo,objInfo;
		if(!(objInfo = SF_FUNC_GetReWriteInfoByObj(OBJ))
			|| !(reWriteInfo = SF_FUNC_GetReWriteConfig(objInfo[SF_REWRITE_INDEX_CLASSID],objInfo[SF_REWRITE_INDEX_MARK_FLAG]))
		)
			return OBJ[ATTR] = STR;
		
		reWriteAttrs = reWriteInfo[SF_REWRITE_CFG_ATTR];
		var curAttrMarked = false;
        for(var i=reWriteAttrs.length;i--;)
        {
            if(reWriteAttrs[i] == ATTR)
            {
                curAttrMarked = true;
                break;
            }
        }
		
        if(curAttrMarked)
        {
            OBJ[ATTR] = SF_FUNC_InsertCookieFlag(SF_FUNC_transhttpurl(STR,1,window.document.SF_g_conofcurloc,true));
			return STR;
		}
		else
			return OBJ[ATTR] = STR;
    }
    
    //get config info of the keyword
	var keyarr = SF_const_scriptkeys[ATTR];
	var attrName	= SF_func_trim(keyarr[0]);
	var filterType  = keyarr[1];
	var funcName	= SF_func_trim(keyarr[2]);
	var limitObjType = keyarr[5];
	var objType = SF_func_getobjtype(OBJ);

	if(typeof OBJ[attrName] == "undefined")
	{
		if(!(objType == SF_TYPE_ELEMENT && OBJ.tagName.toUpperCase()=="XML"))
			return OBJ[attrName] = STR;
	}
	
	if(funcName == "func")
	{
		 return OBJ[attrName] = STR
	}
	
	if(limitObjType != "" && limitObjType.indexOf(objType) < 0)
	{
		if(limitObjType==="windowdocument"
			&&typeof(STR)==="object"
			&&STR.hasOwnProperty("href")
		){//deal width window.location = window.location;
			STR = SF_FUNC_transhttpurl(STR["href"],1);
		}
		return OBJ[attrName] = STR;
	}
	
	var tagNameLwr = "undefined";
	var cmd = "";//WARNING:NOT USE
	if(attrName == "host" || attrName == "pathname" || attrName == "protocol" || attrName == "port" || attrName == "hostname" || attrName == "hash" || attrName=="search")
	{
		if(objType != SF_TYPE_LOCATION && typeof(OBJ["href"]) == "undefined")
			return OBJ[attrName] = STR;
		//if the location obj is constuctor by ourselves.
		if(typeof(OBJ["SF_WINDOW"])!="undefined" && typeof(OBJ["SF_WINDOW"]["location"])!="undefined")
		{	
			if(attrName == "hash")
			{
				if(STR.charAt(0)!='#')//hash in opera should be set by location.hash not location.href,or will cause file load abnormally.
					STR = "#" + STR;
				OBJ["hash"] =  STR;
				OBJ["SF_WINDOW"]["location"]["hash"] = STR;
			}
			else
				OBJ["SF_WINDOW"]["location"]["href"] = SF_FUNC_SET_locattr(OBJ,attrName,STR);
			
			SF_FUNC_GetCustomLocation(OBJ["SF_WINDOW"],false);//update the custom location obj;
			//custom obj like hash & search need prefix ? , #
		}
		else
		{
			if(attrName == "hash")
				OBJ["hash"] =  STR;
			else
				OBJ["href"] = SF_FUNC_SET_locattr(OBJ,attrName,STR);
			
			if(window.location === OBJ)
				SF_FUNC_GetCustomLocation(window,false);//update the custom location obj;
			else
			{
					//cann't not find the window object;
			}
		}
		return STR;
	}
	else if(funcName != "" && typeof(eval) == "function")
	{
        funcName = "SF_FUNC_SET_" + funcName;
		if(typeof(this[funcName]) == "function")
		{  
			var execCode =  funcName + "(OBJ,STR);";
			return eval(execCode);
		}
	}  
	else if(attrName == "text")
	{
		if( objType == SF_TYPE_ELEMENT && OBJ.tagName.toLowerCase() == 'script' )
		{
			var scriptType = SF_REWRITE_TYPE_JS;
			if((OBJ.getAttribute("language") && /vb/.test(OBJ.getAttribute("language")))
				||(OBJ.getAttribute("lang") && /vb/.test(OBJ.getAttribute("lang")))
			)
				scriptType = SF_REWRITE_TYPE_VBS
			
			OBJ[attrName] = SF_FUNC_Svr_Rewrite(STR,scriptType);
			return  STR;
		}
		
		return OBJ[attrName] = STR;
	}
	
	var isFrame = false;
	switch (objType)
	{
		case SF_TYPE_WINDOW:
		{
			if(attrName != "location")
				return OBJ[attrName] = STR;
			else if(!SF_FUNC_IsUndefined(OBJ["location"]["SF_WINDOW"]) || typeof(STR)!="string")//if obj.location is custom or str is not string.
			{//if it's not a custom location and str is string,old routine will rewrite the str and return it.
				var navTarget;
				if(typeof(STR)=="string")
					navTarget = STR;
				else if(SF_func_getobjtype(STR) == SF_TYPE_LOCATION)//if STR is locaton.
				{
						if(SF_FUNC_IsUndefined(STR["SF_WINDOW"]))//origin location
							navTarget = STR["href"];
						else	
							navTarget = STR["SF_WINDOW"]["location"]["href"];//custom locaiton
						
						navTarget = SF_FUNC_revertmyurl(navTarget);
				}
				else
					return OBJ[attrName] = STR;
				
				if(SF_FUNC_IsUndefined(OBJ["location"]["SF_WINDOW"]))//orgin location
				{
					OBJ[attrName] = SF_FUNC_transhttpurl(navTarget,filterType,window.document.SF_g_conofcurloc);
					SF_FUNC_GetCustomLocation(OBJ,false);
					return STR;
				}
				else//custom location,need redirect and update the obj manully.
				{
					OBJ["location"]["SF_WINDOW"]["location"] = SF_FUNC_transhttpurl(navTarget,filterType,window.document.SF_g_conofcurloc);
					SF_FUNC_GetCustomLocation(OBJ,false);
					return STR;
				}
			}
			break;
		}
		case SF_TYPE_LOCATION:
		{
			if (attrName != "href")
				return OBJ[attrName] = STR;
			
			var dstUrl = SF_FUNC_transhttpurl(STR,filterType,window.document.SF_g_conofcurloc);
			if(typeof(OBJ["SF_WINDOW"])!="undefined" && typeof(OBJ["SF_WINDOW"]["location"])!="undefined")
			{
				OBJ["SF_WINDOW"]["location"] = dstUrl;
				SF_FUNC_GetCustomLocation(OBJ["SF_WINDOW"],false);
				return STR;
			}
			else
			{
				OBJ["href"] = dstUrl;
				if(OBJ === window.location)
					SF_FUNC_GetCustomLocation(window,false);
				else
				{
					//braveboy,like iframe.location.href='xxx?hash',just update the hash.need to update custom obj.
					//but here can not find the window of the location.
				}
				
			}
			
			return STR;
		}
		case SF_TYPE_DOCUMENT:
		{
			if (attrName === "cookie")
			{
				return SF_FUNC_SET_cookie(OBJ,STR);
			}
			else if (attrName === "domain")
			{
				return SF_FUNC_SET_domain(OBJ,STR)
			}else if(attrName === "location"){
				if(typeof(STR)==="object"
					&&STR.hasOwnProperty("href")
				){//deal width document.location = window.location;
					STR = SF_FUNC_transhttpurl(STR["href"],1);
					return OBJ[attrName] = STR;
				}
			}
			break;
		}
		case SF_TYPE_ELEMENT:
		case SF_TYPE_BODY:
		{	
			if (attrName == "innerHTML" || attrName == "outerHTML")
				return SF_FUNC_SET_element_html(OBJ,attrName,STR,0);
			
			tagNameLwr = OBJ.tagName.toLowerCase();
			switch (tagNameLwr)
			{
				case "script":
				{
					filterType = 3;
					break;
				}
				case "stylesheet":
				{
					filterType = 2;
					break;
				}
				case "img":
				{
					filterType = -1;
					break;
				}
				case "form":filterType = 1;break;
				case "frame":
				case "iframe":
				{
					isFrame = true;
					filterType = 1;
					break;
				}
				default:
				{

					break;
				}
			}
			break;
		}
		case SF_TYPE_STYLE:
		{
			if(SF_func_trim(STR) != "")
			{
				OBJ[attrName] = SF_FUNC_Svr_Rewrite(STR,SF_REWRITE_TYPE_CSS);
				return STR;
			}
			else
				return OBJ[attrName] =STR;
		}
		//Add Support to create style tags dynamicly
		case SF_TYPE_STYLESHEET:
		{
			if(SF_func_trim(STR) != "")
				return OBJ[attrName] = SF_FUNC_Svr_Rewrite(STR,SF_REWRITE_TYPE_CSS);
			else
				return OBJ[attrName] = STR;
		}
		default:
		{
			return OBJ[attrName] = STR;
		}
	}
	
	if(typeof(STR)=="string" && STR.indexOf("javascript:") == 0 && STR.replace(/(javascript:)|;|\s/g,'').length>0)
	{
		var jsCode = SF_FUNC_ChrToHtml(STR.SF_substr(STR.indexOf(":")+1),['<','>']);
		if(isFrame)
			OBJ[attrName] = "javascript:if(parent.window.SF_func_post_http)window['eval'](parent.window.SF_func_post_http('/com/svpn_websvc_functions.js'));SF_IsPageLoaded=true;" + SF_FUNC_HtmlToChr(SF_FUNC_Svr_Rewrite(jsCode, SF_REWRITE_TYPE_JS))+";if(typeof(SF_FUNC_cache_flush)=='function')SF_FUNC_cache_flush();";
		else
			OBJ[attrName] = "javascript:" + SF_FUNC_HtmlToChr(SF_FUNC_Svr_Rewrite(jsCode, SF_REWRITE_TYPE_JS))+";if(typeof(SF_FUNC_cache_flush)=='function')SF_FUNC_cache_flush();";
		return STR;
	}
	
	var dstUrl = SF_FUNC_transhttpurl(STR,filterType,window.document.SF_g_conofcurloc);
	OBJ[attrName] = dstUrl;
	return STR;//As a y=(a=b),
}
function SF_FUNC_GETATT(OBJ,ATTR)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GETATT",arguments);
	if (OBJ == null || ATTR == null ||  ATTR < 0 || ATTR >= SF_const_scriptkeys.length )
	{ 
		return null ;
	}
	 
	if(ATTR < 0 || ATTR >= SF_const_scriptkeys.length )
		return "";
	
	try{
		if(typeof(OBJ) == "number" && OBJ == 0)/*process global variable like location*/
			OBJ = window;
	}catch(e){SF_FUNC_Debug(e.message);}
	
	 /*Process Custom ActiveX Applet SWF attribute rewrite*/
    //if it's a Custom object,ATTR should be a string but not a integer.
    if(typeof(ATTR) == "string")
    {
		var reWriteInfo,objInfo;
		if(!(objInfo = SF_FUNC_GetReWriteInfoByObj(OBJ))
			|| !(reWriteInfo = SF_FUNC_GetReWriteConfig(objInfo[SF_REWRITE_INDEX_CLASSID],objInfo[SF_REWRITE_INDEX_MARK_FLAG]))
		)
			return OBJ[ATTR];
		
		reWriteAttrs = reWriteInfo[SF_REWRITE_CFG_ATTR];
		var curAttrMarked = false;
        for(var i=reWriteAttrs.length;i--;)
        {
            if(reWriteAttrs[i] == ATTR)
            {
                curAttrMarked = true;
                break;
            }
        }
		
        if(curAttrMarked && typeof(OBJ[ATTR])=="string")
            return SF_FUNC_RemoveCookieFlag(SF_FUNC_revertmyurl(OBJ[ATTR]).replace(/\/?$/,''));//replace cookie flag.
        else
			return OBJ[ATTR];
    }
	
	var keyarr = SF_const_scriptkeys[ATTR];
    var attrName	= SF_func_trim(keyarr[0]);
	var filterType  = keyarr[1];
	var funcName	= SF_func_trim(keyarr[2]);
	var limitObjType = keyarr[5];
	var objType = SF_func_getobjtype(OBJ);
	var isSysUrl = false;
	if(typeof OBJ[attrName] == "undefined")
	{
		try{
			if(!(attrName =='all'
				&&objType===SF_TYPE_DOCUMENT
				&&typeof(OBJ[attrName]["length"])=='number'
			))
				return undefined;
		}catch(e){
			return undefined;
		}
	}
	
	//A problem in extjs like if(typeof(xmObj.getElemenetByTagName) == 'undefined')...
	//xmObj.getElemenetByTagName  in extjs will cause a exception and typeof(xmObj.getElemenetByTagName) is "unknown".
	//so can only NULL but not undefined.
	if(typeof OBJ[attrName] == "unknown")
		return null; 
	
	//check object type wether it's specified.
	if(objType == SF_TYPE_UNKNOWN ||(limitObjType != "" && limitObjType.indexOf(objType) < 0))
		return OBJ[attrName];
	
	if(funcName == "func")
	{
		//If let a function pointer like window.open ,whose params has to be rewrited,to another variable ,then modify the function
		if(!SF_FUNC_IsUndefined(keyarr[6]))
		{
			var fName = SF_func_trim(keyarr[0]);
			OBJ["SF_P"+fName] =  OBJ[fName];
			return function(){
				var urlIndex = keyarr[3]>=0?keyarr[3]:0;
				var argType = SF_func_trim(keyarr[6]);
				var argArr = Array.prototype.slice.call(arguments);
				if(argType==SF_TYPE_ARG_URL)
					argArr[urlIndex] = SF_FUNC_transhttpurl(argArr[urlIndex],1,window.document.SF_g_conofcurloc);
				else if(argType==SF_TYPE_ARG_JS)
					argArr[urlIndex] = SF_FUNC_Svr_Rewrite(argArr[urlIndex],SF_REWRITE_TYPE_JS);
				else if(argType==SF_TYPE_ARG_VB)
					argArr[urlIndex] = SF_FUNC_Svr_Rewrite(argArr[urlIndex],SF_REWRITE_TYPE_VB);
				else if(argType==SF_TYPE_ARG_HTML)
					argArr[urlIndex] = SF_FUNC_Svr_Rewrite(argArr[urlIndex],SF_REWRITE_TYPE_HTML);
				else 
				{
					//unknow type.like setattribute ,need to be process by specified condition.
				}
				
				
				return SF_FUNC_Eval_Function(OBJ,"SF_P"+fName,argArr);
			};
		}
		else
		{
			return OBJ[attrName];
		}
    }
	else if(attrName == "all"
		|| attrName == "anchors"
		|| attrName == "applets"
		|| attrName == "childNodes"
		|| attrName == "embeds"
		|| attrName == "forms"
		|| attrName == "frames"
		|| attrName == "images"
		|| attrName == "links"
		|| attrName == "namespaces"
		|| attrName == "scripts"
		|| attrName == "styleSheets"
		|| attrName == "body"
	){
		SF_FUNC_cache_flush();
		return OBJ[attrName];
	}
	else if (SF_func_isurlcontent(OBJ[attrName]) == false )
		return  OBJ[attrName];
	else if(attrName == "host" || attrName == "pathname" || attrName == "protocol" || attrName == "port" || attrName == "hostname")
	{
		if((objType != SF_TYPE_LOCATION && typeof(OBJ["href"]) == "undefined") || !SF_FUNC_IsUndefined(OBJ["SF_WINDOW"]))
			return OBJ[attrName];
		return SF_FUNC_GET_locattr(OBJ["href"],attrName);  
	}
	else if(attrName == "location")
	{
		if(SF_func_getobjtype(OBJ) == SF_TYPE_WINDOW || SF_func_getobjtype(OBJ) == SF_TYPE_DOCUMENT)
			return SF_FUNC_GetCustomLocation(OBJ,true);
	
	}
	else if(attrName == "origin")
	{
		var ret,curloc;
		if(typeof(OBJ.data)=="string" && (ret = OBJ.data.match(/([\s\S]*)@SF_ORIGIN=([\s\S]+)$/i)))
			return ret[2];
		
		//can't find the realOrigin from the event.data,use the current origin.
		curloc = SF_g_conofcurloc || window.document.SF_g_conofcurloc;
		return curloc["location"]["protocol"] + "//" + curloc["location"]["host"];
	}
	else if(attrName == "data")
	{
		if(SF_func_getobjtype(OBJ) != SF_TYPE_EVENT)//can be event or other dom element has data attribute.It should be differ here.
			return OBJ[attrName];
	
		var ret;
		if(typeof(OBJ.data)=="string")
			return OBJ.data.replace(/@SF_ORIGIN=[\s\S]+$/i,'');
		
		return OBJ.data;
	}
	else if(funcName != "" && typeof(eval) == "function")
	{
		funcName = "SF_FUNC_GET_" + funcName;
		if(typeof(this[funcName]) == "function")
		{  
			var execCode =  funcName + "(OBJ);";
			return eval(execCode);
		}
	}	  
	switch (objType)
	{
		case SF_TYPE_LOCATION:
		{
			if (attrName != "href")
			{
				return OBJ[attrName];
			}
			isSysUrl = true;
			break;
		}
		case SF_TYPE_DOCUMENT:
		{
			if (attrName == "cookie")
			{
				return SF_FUNC_GET_cookie(OBJ);
			}
			else if (attrName == "domain")
			{
				return SF_FUNC_GET_domain(OBJ);
			}
            else if(attrName == "location")/*WARNING:NOT USE NOW*/
                return SF_FUNC_GET_location();
			else if(attrName == "URL")
				isSysUrl = true;
			break;
		}
		case SF_TYPE_ELEMENT:
		case SF_TYPE_BODY:
		{
			if (attrName == "innerHTML" || attrName == "outerHTML" || attrName == "value" ||  attrName == "innerText" )
				return SF_FUNC_GET_element_html(OBJ,attrName);
			else if(attrName == "href")
			{
				if(typeof(OBJ[attrName]) == "string" && /^javascript:/.test(OBJ[attrName]))
				{
					return OBJ[attrName].replace(/(var)?\sSF_FUNC_cache_flush_tmp=SF_FUNC_cache_flush;|SF_FUNC_cache_flush_tmp\(\);/g,'');
				}
				if(OBJ.tagName.toLowerCase() == "a" || OBJ.tagName.toLowerCase() == "area")
					return SF_FUNC_revertmyurl(OBJ[attrName],null,true);
			}
			break;
		}
		default:
		{
			return OBJ[attrName];
		}
	}
	var oriVal = OBJ[attrName];
	var newVal = SF_FUNC_revertmyurl(oriVal,null,isSysUrl);
	return newVal;
}

function SF_FUNC_APPENDATT(OBJ,ATTR,STR)
{
	SF_FUNC_RoutineTrace("SF_FUNC_APPENDATT",arguments);
	if (OBJ == null || ATTR == null ||  ATTR < 0 || ATTR >= SF_const_scriptkeys.length )
	{
		return STR;
	}
	/*Process Custom ActiveX Applet SWF attribute rewrite*/
    //if it's a Custom object,ATTR should be a string but not a integer.
    if(typeof(ATTR) == "string")
    {
		var reWriteInfo,objInfo;
		if(!(objInfo = SF_FUNC_GetReWriteInfoByObj(OBJ))
			|| !(reWriteInfo = SF_FUNC_GetReWriteConfig(objInfo[SF_REWRITE_INDEX_CLASSID],objInfo[SF_REWRITE_INDEX_MARK_FLAG]))
		)
			return (OBJ[ATTR]=OBJ[ATTR]+STR);
		
		reWriteAttrs = reWriteInfo[SF_REWRITE_CFG_ATTR];
		var curAttrMarked = false;
        for(var i=reWriteAttrs.length;i--;)
        {
            if(reWriteAttrs[i] == ATTR)
            {
                curAttrMarked = true;
                break;
            }
        }
		
        if(curAttrMarked && typeof(OBJ[ATTR])=="string")
		{
			return SF_FUNC_SETATT(OBJ,ATTR,SF_FUNC_GETATT_CUS(OBJ,ATTR,SF_ReWriteObjsConfig) + STR);
		}
		else
			return (OBJ[ATTR]= OBJ[ATTR] + STR);
    }
	
	var keyarr = SF_const_scriptkeys[ATTR];
	var attrName = keyarr[0];
	var oldval = SF_FUNC_GETATT(OBJ,ATTR);
	return SF_FUNC_SETATT(OBJ,ATTR,oldval + STR);
}

function SF_FUNC_IsArray(obj)
{
  try{
        //return obj.constructor == Array;there is a bug when the object is from another frame.
		return (typeof(obj.length) === 'number' && (typeof(obj.splice) === 'function' || typeof(obj.splice) === 'object'));
    }catch(e)
    {
		SF_FUNC_Debug(e.message);
        return false;
    }
}

function SF_FUNC_GetKeyInfo(index)
{
	SF_FUNC_INIT_GlobalVars();
	if(index < 0 || index >= SF_const_scriptkeys.length )
		return null;
	return SF_const_scriptkeys[index];
}


function SF_FUNC_GetKeyInfoByName(name)
{
	SF_FUNC_INIT_GlobalVars();
	for(var i=0;i<SF_const_scriptkeys.length ;i++)
	{
		if(SF_const_scriptkeys[i][0] == name)
			return {"info":SF_const_scriptkeys[i],"index":i};
	}
	return null;
}

/** 
    *function XXX_CUS is used to deal with custom object rewirte
	*SF_FUNC_CALLFUNC_CUS:Call fun from custom object.
    *param
			OBJ[Object]:custom object
			FUNC[String]:function name of the object
			CONFIG[Array]:Current rewrite config.This argument is not used now.Just use for Server 
						  side rewrite to find a proper position to place the rewrite config expression.
    *retvalue:executed result;
	*SF_FUNC_CALLFUNC_CUS(obj,"myfunc",cfg,this,args)
**/
function SF_FUNC_CALLFUNC_CUS(OBJ,FUNC,CONFIG,THIS_OBJ)
{
	SF_FUNC_RoutineTrace("SF_FUNC_CALLFUNC_CUS",arguments);
	if(arguments.length < 4 || SF_FUNC_IsUndefined(OBJ) || typeof(FUNC)!="string")
		throw SF_FUNC_Debug("SF_FUNC_CALLFUNC_CUS:Parameter err!");
	
	if(CONFIG)
	{
		var argArr = Array.prototype.slice.call(arguments,0,2).concat(Array.prototype.slice.call(arguments,3));
		var AttRewriteInfo = SF_FUNC_GetReWriteConfigItem(OBJ,FUNC,SF_KEY_TYPE_FUNC);//now can not differ from SF_KEY_TYPE_LEFT to SF_KEY_TYPE_RIGHT because console don't have details about these.
		
		if(AttRewriteInfo)//rewrite info exists.
			return SF_FUNC_CALLFUNC.apply(this,argArr);
		else
		{
			var keyInfo = SF_FUNC_GetKeyInfoByName(FUNC);
			if(keyInfo  &&  keyInfo["info"][7] == SF_KEY_TYPE_FUNC)//SF_FUNC_CALLFUNC match expression type.
			{
				argArr[1] = keyInfo["index"];
				return SF_FUNC_CALLFUNC.apply(this,argArr);
			}
			else//evalute the expression now.
				return SF_FUNC_Eval_Function(OBJ,FUNC,Array.prototype.slice.call(arguments,4));
		}
	
	}
	else return SF_FUNC_Eval_Function(OBJ,FUNC,Array.prototype.slice.call(arguments,4));
}

/** 
    *SF_FUNC_SETATT_CUS:set property value to custom object.
    *param
			OBJ[Object]:custom object
			ATTR[String]:property name of the object
			CONFIG[Array]:Current rewrite config.This argument is not used now.Just use for Server 
						  side rewrite to find a proper position to place the rewrite config expression.
    *retvalue:rewrited value;
**/
function SF_FUNC_SETATT_CUS(OBJ,ATTR,CONFIG,STR)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SETATT_CUS",arguments);
	if(arguments.length!=4 || SF_FUNC_IsUndefined(OBJ) || typeof(ATTR)!="string")
	{
		SF_FUNC_Debug("SF_FUNC_SETATT_CUS:Parameter err!");
		return (OBJ[ATTR] = STR);
	}

	var AttRewriteInfo = SF_FUNC_GetReWriteConfigItem(OBJ,ATTR,SF_KEY_TYPE_LEFT_RIGHT);//now can not differ from SF_KEY_TYPE_LEFT to SF_KEY_TYPE_RIGHT because console don't have details about these.
	if(AttRewriteInfo)//rewrite info exists.
		return SF_FUNC_SETATT(OBJ,ATTR,STR);
	else
	{
		var keyInfo = SF_FUNC_GetKeyInfoByName(ATTR);
		if(keyInfo  
			&&  (keyInfo["info"][7] == SF_KEY_TYPE_LEFT || keyInfo["info"][7] == SF_KEY_TYPE_LEFT_RIGHT)
		)//SF_FUNC_SETATT match expression type.
			return SF_FUNC_SETATT(OBJ,keyInfo["index"],STR);
		else//evalute the expression now.
			return OBJ[ATTR] = STR;
	}
}

/** 
    *SF_FUNC_GETATT_CUS:get property value from custom object.
    *param
			OBJ[Object]:custom object
			ATTR[String]:property name of the object
			CONFIG[Array]:Current rewrite config.This argument is not used now.Just use for Server 
						  side rewrite to find a proper position to place the rewrite config expression.
    *retvalue:rewrited value;
**/
function SF_FUNC_GETATT_CUS(OBJ,ATTR,CONFIG)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GETATT_CUS",arguments);
	if(arguments.length!=3 || SF_FUNC_IsUndefined(OBJ) || typeof(ATTR)!="string")
	{
		SF_FUNC_Debug("SF_FUNC_GETATT_CUS:Parameter err!");
		return OBJ[ATTR];
	}
	
	var AttRewriteInfo = SF_FUNC_GetReWriteConfigItem(OBJ,ATTR,SF_KEY_TYPE_LEFT_RIGHT);//now can not differ from SF_KEY_TYPE_LEFT to SF_KEY_TYPE_RIGHT because console don't have details about these.
	if(AttRewriteInfo)//rewrite info exists.
		return SF_FUNC_GETATT(OBJ,ATTR);
	else
	{
		var keyInfo = SF_FUNC_GetKeyInfoByName(ATTR);
		if(keyInfo  
			&&  (keyInfo["info"][7] == SF_KEY_TYPE_RIGHT || keyInfo["info"][7] == SF_KEY_TYPE_LEFT_RIGHT)
		)//SF_FUNC_GETATT match expression type.
			return SF_FUNC_GETATT(OBJ,keyInfo["index"]);
		else//evalute the expression now.
			return OBJ[ATTR];
	}
}

/** 
    *SF_FUNC_APPENDATT_CUS:append property value to custom object.
    *param
			OBJ[Object]:custom object
			ATTR[String]:property name of the object
			CONFIG[Array]:Current rewrite config.This argument is not used now.Just use for Server 
						  side rewrite to find a proper position to place the rewrite config expression.
    *retvalue:rewrited value;
**/
function SF_FUNC_APPENDATT_CUS(OBJ,ATTR,CONFIG,STR)//temporary not use,for future implemention of keword with 
{
	SF_FUNC_RoutineTrace("SF_FUNC_APPENDATT_CUS",arguments);
	if(arguments.length!=4 || SF_FUNC_IsUndefined(OBJ) || typeof(ATTR)!="string")
	{
		SF_FUNC_Debug("SF_FUNC_APPENDATT_CUS:Parameter err!");
		return OBJ[ATTR] = OBJ[ATTR] + STR;;
	}
	
	var AttRewriteInfo = SF_FUNC_GetReWriteConfigItem(OBJ,ATTR,SF_KEY_TYPE_LEFT_RIGHT);//now can not differ from SF_KEY_TYPE_LEFT to SF_KEY_TYPE_RIGHT because console don't have details about these.
	if(AttRewriteInfo)//rewrite info exists.
		return SF_FUNC_APPENDATT(OBJ,ATTR,STR);
	else
	{
		var keyInfo = SF_FUNC_GetKeyInfoByName(ATTR);
		if(keyInfo &&  (keyInfo["info"][7] == SF_KEY_TYPE_LEFT || keyInfo["info"][7] == SF_KEY_TYPE_LEFT_RIGHT))//SF_FUNC_APPENDATT match expression type.
			return SF_FUNC_APPENDATT(OBJ,keyInfo["index"],STR);
		else//evalute the expression now.
			return OBJ[ATTR] = OBJ[ATTR] + STR;
	}
}

/*function XXX_DEF is used to deal with key word without obj
**e.g open(url),xx=location,location=xx.
**SF_FUNC_CALLFUNC_CUS(obj,"myfunc",cfg,this,args)
**SF_FUNC_CALLFUNC_DEF(0,index,this,0,location,args)
*/
function SF_FUNC_CALLFUNC_DEF(OBJ,FUNC,THIS_OBJ,FUN_POINT,HAS_NEW)
{
	SF_FUNC_RoutineTrace("SF_FUNC_CALLFUNC_DEF",arguments);
	var objInfo ;
	if(!(objInfo = SF_FUNC_GetKeyInfo(FUNC)))
		return SF_FUNC_Eval_Function(OBJ,objInfo[0],Array.prototype.slice.call(arguments,3));
	else
	{
		if(OBJ===0)
			OBJ = window;
			
		var funName = objInfo[0];
		//fix .htc bug
		if(FUN_POINT&&THIS_OBJ.document&&THIS_OBJ.element&&THIS_OBJ.document.mimeType&&THIS_OBJ.document.mimeType.indexOf("HTC")===0&&funName == 'ActiveXObject')
		{
			var argArr = Array.prototype.slice.call(arguments,5);
			var rvObj;
			var evalarr = ["FUN_POINT("];
			var i = 0;
			for(i=0;i<argArr.length;i++)
			{
				if(i==0){
					evalarr.push('argArr['+i+']');
				}else{
					evalarr.push(',argArr['+i+']');
				}
			}
			evalarr.push(');');
			if(HAS_NEW){
				evalarr.unshift('new ');
			}
			evalarr.unshift('rvObj = ');
			eval(evalarr.join(''));
			
			SF_FUNC_AddReWriteObjs(rvObj,argArr[0],SF_OBJECT_LABEL_OBJECT);
			return rvObj;
		}
		//if current FUN_POINT is not the obj.funPoint,means OBJ is not the owner of FUN_POINT.
		//there is no need to deal with the conditon.In VBScript FUN_POINT is null.
		if(!FUN_POINT || OBJ[funName] == FUN_POINT || document[funName] == FUN_POINT)//TD21511
		{
			var argArr = [];
			if(funName == 'Function' || funName == 'ActiveXObject')//TD21675
			{
				argArr = Array.prototype.slice.call(arguments,0,3).concat(Array.prototype.slice.call(arguments,4));
			}
			else
			{
				argArr = Array.prototype.slice.call(arguments,0,3).concat(Array.prototype.slice.call(arguments,5));
			}
			return SF_FUNC_CALLFUNC.apply(this,argArr);
		}
		else return SF_FUNC_EXEC_FUNOP(FUN_POINT,HAS_NEW,THIS_OBJ,Array.prototype.slice.call(arguments,5));
	}
}

function SF_FUNC_EXEC_FUNOP(funPt,hasNew,thisObject,args)
{
	if(!hasNew)
	{
		if(typeof(funPt)==='function'){
			return funPt.apply(thisObject,args);
		}else{
			switch(args.length)
			{
				case 0:return  funPt();break;
				case 1:return  funPt(args[0]);break;
				case 2:return  funPt(args[0],args[1]);break;
				case 3:return  funPt(args[0],args[1],args[2]);break;
				case 4:return  funPt(args[0],args[1],args[2],args[3]);break;
				case 5:return  funPt(args[0],args[1],args[2],args[3],args[4]);break;
				case 6:return  funPt(args[0],args[1],args[2],args[3],args[4],args[5]);break;
				case 7:return  funPt(args[0],args[1],args[2],args[3],args[4],args[5],args[6]);break;
				case 8:return  funPt(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7]);break;	
			}			
		}
	}
	
	switch(args.length)
	{
		case 0:return new funPt();break;
		case 1:return new funPt(args[0]);break;
		case 2:return new funPt(args[0],args[1]);break;
		case 3:return new funPt(args[0],args[1],args[2]);break;
		case 4:return new funPt(args[0],args[1],args[2],args[3]);break;
		case 5:return new funPt(args[0],args[1],args[2],args[3],args[4]);break;
		case 6:return new funPt(args[0],args[1],args[2],args[3],args[4],args[5]);break;
		case 7:return new funPt(args[0],args[1],args[2],args[3],args[4],args[5],args[6]);break;
		case 8:return new funPt(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7]);break;
	}
}

function SF_FUNC_SETATT_DEF(OBJ,ATTR,ATT_POINT,STR)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SETATT_DEF",arguments);
	var objInfo = SF_FUNC_GetKeyInfo(ATTR);
	if(!objInfo)
		return (OBJ[ATTR] = STR);
	else
	{
		if(OBJ===0)
			OBJ = window;
			
		var attrName = objInfo[0];
		
		if(OBJ[attrName] == ATT_POINT)
			return SF_FUNC_SETATT(OBJ,ATTR,STR);
		else 
			return (OBJ[ATTR] = STR);
	}
}

function SF_FUNC_GETATT_DEF(OBJ,ATTR,ATT_POINT)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GETATT_DEF",arguments);
	var objInfo = SF_FUNC_GetKeyInfo(ATTR);
	if(!objInfo)
		return ATT_POINT;
	else
	{
		if(OBJ===0)
			OBJ = window;
		var attrName = objInfo[0];
		if(OBJ[attrName] == ATT_POINT)
			return SF_FUNC_GETATT(OBJ,ATTR);
		else 
			return ATT_POINT;
	}
}

function SF_FUNC_APPENDATT_DEF(OBJ,ATTR,ATT_POINT,STR)//temporary not use,for future implemention of keword with 
{
	SF_FUNC_RoutineTrace("SF_FUNC_APPENDATT_DEF",arguments);
	var objInfo = SF_FUNC_GetKeyInfo(ATTR);
	if(!objInfo)
		return (ATT_POINT +=STR);
	else
	{
		if(OBJ===0)
			OBJ = window;
		var attrName = objInfo[0];
		if(OBJ[attrName] == ATT_POINT)
			return SF_FUNC_APPENDATT(OBJ,ATTR,STR);
		else 
			return (ATT_POINT +=STR);
	}
}
/*
	SF_FUNC_CALLFUNC(obj,index,this,args)
*/
function SF_FUNC_CALLFUNC(OBJ,FUNC,THIS_OBJ)
{
	
	SF_FUNC_RoutineTrace("SF_FUNC_CALLFUNC",arguments);
	SF_FUNC_INIT_GlobalVars();
	if (OBJ == null || FUNC== null ||  typeof OBJ == "undefined" || arguments.length < 2)
		return;
	
	try{
		if(typeof(OBJ) == "number" && OBJ == 0)/*process global call without windws object explicit,like open(xxx);*/
			OBJ = window;
	}catch(e){SF_FUNC_Debug(e.message);}
	
	var keyarr,funcName,szObjectType;
    var myargs = new Array();
	var myarglen = 0;
	var RetVal = "";  
    var urlPos =null;
	var funcArgLen = null;
	var limitObjType = "";
    var filterType = null;
    //prepare args;
    /*for (myarglen = 0;myarglen < arguments.length - 2;myarglen++)
	{
		myargs[myarglen] = arguments[myarglen + 2];
	}
	*/
	var thisObj = THIS_OBJ;
	myargs = Array.prototype.slice.call(arguments,3);
	myarglen = myargs.length;
    //process normal function call;
    if( typeof(FUNC) == "number")
	{
        if(FUNC < 0 || FUNC >= SF_const_scriptkeys.length )
        {
            return ;
        }
        var keyarr = SF_const_scriptkeys[FUNC];
        if(keyarr[2] != "func")
        {
             return ;
        }
    
        funcName = SF_func_trim(keyarr[0]);
        filterType = keyarr[1];
        szObjectType = SF_func_getobjtype(OBJ);
        urlPos = keyarr[3];
        funcArgLen = keyarr[4];
        limitObjType = keyarr[5];
	}
    else if(typeof(FUNC) == "string")//process function call from plugin object
    {
		var objInfo,reWriteInfo;
        if(   (objInfo = SF_FUNC_GetReWriteInfoByObj(OBJ))
			&& (reWriteInfo = SF_FUNC_GetReWriteConfig(objInfo[SF_REWRITE_INDEX_CLASSID],objInfo[SF_REWRITE_INDEX_MARK_FLAG]))
			&& !SF_FUNC_IsUndefined(reWriteInfo[SF_REWRITE_CFG_FUN][FUNC])
		)
		{
			szObjectType = SF_TYPE_OBJECT; 
			filterType = 1;
			urlPos = reWriteInfo[SF_REWRITE_CFG_FUN][FUNC];
			funcArgLen = myarglen;
			
			if(SF_FUNC_IsArray(urlPos))
			{
				for(var i=urlPos.length;i--;)
					myargs[urlPos[i] - 1] = SF_FUNC_InsertCookieFlag(SF_FUNC_transhttpurl(myargs[urlPos[i] - 1],filterType,window.document.SF_g_conofcurloc,true));
			}
			else
				myargs[urlPos - 1] = SF_FUNC_InsertCookieFlag(SF_FUNC_transhttpurl(myargs[urlPos - 1],filterType,window.document.SF_g_conofcurloc,true));
			
		}
		
		return SF_FUNC_Eval_Function(OBJ,FUNC,myargs);
    }
    else
        return;
    
	if(funcName == "eval")
    {
		var reWritedTxt = SF_FUNC_Svr_Rewrite(myargs[0],SF_REWRITE_TYPE_JS);
        eval(reWritedTxt);
        return ;
    }
	else if(funcName == "write" || funcName == "writeln")
	{
		var intext = "";
        if(szObjectType != SF_TYPE_DOCUMENT)
		{
			if(funcName == "write")
			{
                return SF_FUNC_Eval_Function(OBJ,"write",myargs);
			}else{
				return SF_FUNC_Eval_Function(OBJ,"writeln",myargs);
			}
		}
        
        for(var i = 0;i < myarglen;i++)
		{
			intext += myargs[i];
		}
		if(FUNC == 3)
		{
			intext += "\n";
		}
		if(!OBJ.__sf_has_this__)
		{ 
			OBJ.__sf_has_this__ = true;
            try
			{
 				if(!SF_func_checkurliftransed(OBJ.location))	
				{
					OBJ.__sf_location__ = SF_FUNC_GetLocation_Script_Compatible();
				}
				SF_func_init_docvar(OBJ);
			}catch(e){SF_FUNC_Debug(e.message);}
			OBJ.SF_g_doctext = "";
		}
		var ret = SF_FUNC_doc_write(OBJ,intext);
		return ret;
	}
	else if (funcName == "setAttribute")
	{
		if (myargs.length != 2
			|| SF_FUNC_IsUndefined(myargs[0]) || SF_FUNC_IsUndefined(myargs[1])
			|| !SF_FUNC_IsEleAttributte(OBJ,myargs) 
			)
			return SF_FUNC_Eval_Function(OBJ,"setAttribute",myargs);
		
		var tagNameLwr = OBJ.tagName.toLowerCase();	
		var filterType = 0;
		switch (tagNameLwr)
		{
			case "script":
			{
				filterType = 3;
				break;
			}
			case "stylesheet":
			{
				filterType = 2;
				break;
			}
			default:
			{
				break;
			}
		}
		
		if(myargs[0].toLowerCase() == "innerhtml")
			myargs[1] = SF_FUNC_Svr_Rewrite(myargs[1],SF_REWRITE_TYPE_HTML);
		else if(typeof(myargs[1])=="string" && myargs[1].indexOf("javascript:") == 0)
		{
		    var srcCode = myargs[1].SF_substr(myargs[1].indexOf(":")+1);
			myargs[1] = "javascript:" + SF_FUNC_Svr_Rewrite(srcCode, SF_REWRITE_TYPE_JS);
		}
		else
		{
			myargs[1] = SF_FUNC_transhttpurl(myargs[1],filterType,window.document.SF_g_conofcurloc);
		}
			
		return OBJ["setAttribute"](myargs[0],myargs[1]);
	}
	else if (funcName == "getAttribute")
	{
		var retValue = SF_FUNC_Eval_Function(OBJ,"getAttribute",myargs);
		if (!retValue || !SF_FUNC_IsEleAttributte(OBJ,myargs) || myargs.length < 1 || myargs.length > 2)
			return retValue;
		
		if( typeof(myargs[0]) != "string")
			return retValue;
			
		if(myargs[0].match(/^on\w+$/i))
		{
			if(typeof(retValue) == "string"){//some custom event like "onMenuClick",must return string;
				retValue = retValue.replace(/((var\s)?SF_FUNC_cache_flush_tmp=SF_FUNC_cache_flush;)|(SF_FUNC_cache_flush_tmp\(\);)/g,"");
				return retValue;
			}
			return retValue;
		}
		else if(myargs[0].toLowerCase()!="innerhtml")
		{
			var tagNameLwr;
			if(typeof(OBJ.tagName)=="string")
				tagNameLwr = OBJ.tagName.toLowerCase();
			
			if(myargs[0].toLowerCase()=='href' 
				&&(tagNameLwr == "a" || tagNameLwr == "area")
				&& SF_Util.isIE 
				&& (SF_FUNC_IsUndefined(document.documentMode) || document.documentMode < 8)
			)
				return SF_FUNC_revertmyurl(retValue,null,true);
			else
				return SF_FUNC_revertmyurl(retValue);
				
			if(tagNameLwr == 'param' || tagNameLwr =="embed") //replace cookie flag.
				retValue = SF_FUNC_RemoveCookieFlag(retValue);
			
			return retValue;
		}
		else
		{
			return retValue;
		}
	}
    else if(funcName.toLowerCase() == "open")
	{
		if(myarglen < 1)
		{
            return OBJ[funcName]();
		}
		switch (szObjectType)
		{
			case SF_TYPE_DOCUMENT:
			case SF_TYPE_WINDOW:
			{
                if(!(typeof(myargs[0])=="string" && myargs[0].toLowerCase() =="text/html"))
					myargs[0] = SF_FUNC_transhttpurl(myargs[0],1,window.document.SF_g_conofcurloc);
				break;
			}
			case SF_TYPE_XMLHTTPREQ:
			case SF_TYPE_ACTIVEX:
			{
				if(myarglen >=2)
				{
					myargs[1] = SF_FUNC_transhttpurl(myargs[1],-1,window.document.SF_g_conofcurloc);
				}
				break;
			}		
			default:
			{
				 ;
			}
		}
		return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
	else if(funcName=="Send")//for bug 15566.correct the constructor changed by SF_FUNC_SangforActiveXObject.
	{
		if(myarglen < 1)
			return OBJ["Send"]();
		var methodName="Send";
		if(szObjectType==SF_TYPE_XMLHTTPREQ
			|| szObjectType==SF_TYPE_ACTIVEX
		)
			methodName = "send";
		return SF_FUNC_Eval_Function(OBJ,methodName,myargs);
	}
	else if (funcName == "attachEvent"
			||funcName == "addEventListener"
	)
	{
		if((myargs.length ==2 || myargs.length ==3)
			&&(szObjectType == SF_TYPE_ELEMENT || szObjectType == SF_TYPE_WINDOW)
		)
		{	
			var tmpFun = myargs[1];
			var eventName = myargs[0];
			myargs[1] = function()
			{
				if(eventName.toLowerCase().indexOf('load') != -1)
					SF_IsPageLoaded = false;
				var SF_FUNC_cache_flush_tmp = SF_FUNC_cache_flush;
				SF_FUNC_INIT_GlobalVars();
				tmpFun.apply(thisObj,arguments);
				SF_FUNC_cache_flush_tmp();
			};
			SF_AttachEvent_handles.push([tmpFun,myargs[1]]);
			if(myargs.length == 2)
				return OBJ[funcName](myargs[0],myargs[1]);
			else if(myargs.length == 3)
				return OBJ[funcName](myargs[0],myargs[1],myargs[2]);
		}
		else
			return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
	else if (funcName == "detachEvent"
			||funcName == "removeEventListener"
	)
	{
		if((myargs.length ==2 || myargs.length ==3)
		&&(szObjectType == SF_TYPE_ELEMENT || szObjectType == SF_TYPE_WINDOW)
		)
		{
			var tmpFun = myargs[1]
			for(var i=0;i<SF_AttachEvent_handles.length;i++)
			{
				if(tmpFun == SF_AttachEvent_handles[i][0])
				{
					tmpFun = SF_AttachEvent_handles[i][1];
					while(i<(SF_AttachEvent_handles.length-1))
					{
						SF_AttachEvent_handles[i] = SF_AttachEvent_handles[i+1];
						i++;
					}
					SF_AttachEvent_handles.length--;
					break;
				}
			}
			if(myargs.length == 2)
				return OBJ[funcName](myargs[0],tmpFun);
			else if(myargs.length == 3)
				return OBJ[funcName](myargs[0],tmpFun,myargs[2]);
		}
		else
			return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
	else if(funcName == "createElement"
			&& SF_TYPE_DOCUMENT == szObjectType
	)
	{//some unstandard code will use createElement('<img src="url">'),in this case ,rewrite the parameter.
			if(myargs.length == 1 
			&& myargs[0].constructor == String
			&& /<\w+[^<>]+\/?>/.test(myargs[0])
			)
			{
				myargs[0] = SF_FUNC_Svr_Rewrite(myargs[0],SF_REWRITE_TYPE_HTML);
				return OBJ[funcName](myargs[0]);
			}
				
	}
	else if(funcName == "setTimeout" ||  funcName == "setInterval")
	{
			if(myargs.length<2 || myargs.length >3 || SF_TYPE_WINDOW != szObjectType)
				return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
			
			var scriptLan  = SF_REWRITE_TYPE_JS;
			if(myargs.length > 2 && typeof(myargs[2])=="string" && /vb/i.test(myargs[2]))
				scriptLan = SF_REWRITE_TYPE_VBS;
				
			if(typeof(myargs[0]) == "string")
			{
				if(scriptLan == SF_REWRITE_TYPE_VBS)
					myargs[0] = "SF_IsTimeOut=SF_IsTimeOut+1\n" + myargs[0] + "\nSF_IsTimeOut=SF_IsTimeOut-1";
				else
					myargs[0] = myargs[0].replace(/^\s*;?/,"SF_IsTimeOut++;\n").replace(/;?\s*$/,";SF_IsTimeOut--;");
				myargs[0] = SF_FUNC_Svr_Rewrite(myargs[0],scriptLan);
			}
			else if(typeof(myargs[0]) == "function")
			{
				var callbackFun = myargs[0];
				myargs[0] = function(){SF_IsTimeOut++;callbackFun();SF_IsTimeOut--;};
			}
			return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
	else if(funcName == "ActiveXObject")
	{
		var hasNewOp;
		if(myargs.length<1)
			return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
		else
		{
			hasNewOp = myargs[0];//first parameter specified the expression is "new OBJ.ActiveXObject(xxx)" or "OBJ.ActiveXObject(xxx)"
			myargs = myargs.slice(1);
		}
		
		if(szObjectType == SF_TYPE_WINDOW)
		{
			if(myargs.length == 3 && typeof(myargs[2])=="string")
				myargs[2] = SF_FUNC_transhttpurl(myargs[2],1,window.document.SF_g_conofcurloc);
			
			return SF_FUNC_Eval_Function(OBJ,"SF_FUNC_SangforActiveXObject",myargs);
		}
		else
			return SF_FUNC_Eval_NewFunction(OBJ,"ActiveXObject",myargs);
	}
	else if(funcName == "Function" )
	{
		var hasNewOp;
		if(myargs.length<1)
			return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
		else
		{
			hasNewOp = myargs[0];//first parameter specified the expression is "new OBJ.Function(xxx)" or "OBJ.Function(xxx)"
			myargs = myargs.slice(1);
		}
		
		if(szObjectType == SF_TYPE_WINDOW)
			return SF_FUNC_Eval_Function(OBJ,"SF_FUNC_SangforFunction",myargs);
		else
			return SF_FUNC_Eval_NewFunction(OBJ,"Function",hasNewOp,myargs);
	}
	else if(funcName == "getElementById" )
	{
		if((myargs.length ==1)
			&&(szObjectType == SF_TYPE_ELEMENT || szObjectType == SF_TYPE_DOCUMENT || szObjectType == SF_TYPE_BODY))
		{
			SF_FUNC_cache_flush();
		}
		
		return SF_FUNC_Eval_Function(OBJ,"getElementById",myargs);
	}
	else if(funcName == "close" )
	{
		if( myargs.length == 0 && szObjectType == SF_TYPE_DOCUMENT)
		{
			if(OBJ !== document)
			{
				SF_FUNC_cache_flush_single(OBJ,SF_FLS_ALL);
			}
			else
			{
				SF_FUNC_cache_flush();
			}
			
		}
		
		return SF_FUNC_Eval_Function(OBJ,"close",myargs);
	}
	else if(funcName == "getElementsByTagName")
	{
		if(!((myargs.length ==1) && (szObjectType == SF_TYPE_ELEMENT || szObjectType == SF_TYPE_DOCUMENT || szObjectType == SF_TYPE_BODY)))
			return SF_FUNC_Eval_Function(OBJ,"getElementsByTagName",myargs);
		
		SF_FUNC_cache_flush();
		var SF_TAG_FLAG = "sf_script";
		var eles = SF_FUNC_Eval_Function(OBJ,"getElementsByTagName",myargs);
		var retEles = new Array();
		var retLength = 0;
		var elesLength = eles.length;
		if(eles != null && typeof eles.length != "undefined")
		{
			for(var j=0; j<elesLength; j++)//if the script is our script, don't include
			{
				if(eles[j].getAttribute(SF_TAG_FLAG) == "1")
					continue;
				retEles[retLength++] = eles[j];
				if(eles[j].id){
					retEles[eles[j].id] = eles[j];
				}
			}
			retEles.item = function(index)
			{
				if(index < this.length)
					return this[index];
				return null;
			};
			retEles.Nameditem = function(name)
			{
				for(var k=0;k<this.length;k++)
					if(this[k].name==name || this[k].id== name)
						return this[k];
				return null;
			};
			if(eles.length != retEles.length)
			{
				return retEles;
			}
		}
		
		return eles;
	}
	else if(funcName == "appendChild")
	{
		if(myargs.length == 1 
			&& szObjectType == SF_TYPE_ELEMENT
			&& OBJ.tagName.toLowerCase() == 'script' 
			&& SF_func_getobjtype(myargs[0]) == SF_TYPE_TEXTNODE)
		{
			var scriptType = SF_REWRITE_TYPE_JS;
			if((OBJ.getAttribute("language") && /vb/.test(OBJ.getAttribute("language")))
				||(OBJ.getAttribute("lang") && /vb/.test(OBJ.getAttribute("lang")))
			)
				scriptType = SF_REWRITE_TYPE_VBS
			
			var nodeScript = SF_FUNC_Svr_Rewrite(myargs[0].substringData(0,myargs[0].length),scriptType);
			myargs[0].replaceData(0,myargs[0].length,nodeScript)
		}
		
		return SF_FUNC_Eval_Function(OBJ,"appendChild",myargs);
	}
	else if(funcName == "postMessage")
	{
		if(myargs.length == 2
			&& szObjectType == SF_TYPE_WINDOW
			&& typeof(myargs[0]) == "string"
			&& typeof(myargs[1]) == "string"
			&& !(SF_Util.isIE5 || SF_Util.isIE6)
		)
		{
			var curloc = SF_g_conofcurloc || window.document.SF_g_conofcurloc;
			myargs[0] += "@SF_ORIGIN=" + curloc["location"]["protocol"] + "//" + curloc["location"]["host"];
			myargs[1] = window.location.protocol + '//' + window.location.host;
		}
		
		return SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
	
	var argStr = "";
	for(var i = 0;i < myarglen - 1;i++)
	{
		argStr += "myargs[" + i.toString() + "]" + ",";
	}
	argStr += "myargs[" + (myarglen - 1) + "]";
	
    
	if(SF_func_isundefined(typeof(urlPos)) || SF_func_isundefined(typeof(urlPos)) || SF_func_isundefined(typeof(limitObjType)))
	{
		return  SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
    
    
	if(typeof(urlPos) == "number" 
		&&(urlPos <= 0 || funcArgLen > 0) 
		&& (myarglen != funcArgLen || myarglen < urlPos)
	)
	{
		 return  SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
	
    if(filterType < 0)
	{ 
		var funcName = "SF_FUNC_CALL_" + funcName;
		var	JsCode = funcName + "(OBJ," + argStr + ")";
		return eval(JsCode)
	}
	if(limitObjType != "" && limitObjType.indexOf(szObjectType) < 0)
	{
		return  SF_FUNC_Eval_Function(OBJ,funcName,myargs);
	}
    
    if(SF_FUNC_IsArray(urlPos))
    {
        for(var i=urlPos.length;i--;)
            myargs[urlPos[i] - 1] = SF_FUNC_transhttpurl(myargs[urlPos[i] - 1],filterType,window.document.SF_g_conofcurloc,true);
    }
    else
        myargs[urlPos - 1] = SF_FUNC_transhttpurl(myargs[urlPos - 1],filterType,window.document.SF_g_conofcurloc,true);
	return  SF_FUNC_Eval_Function(OBJ,funcName,myargs);
}
/** add 20070829 **/

function SF_FUNC_doc_write(pDocObj,inText)
{  
	SF_FUNC_RoutineTrace("SF_FUNC_doc_write",arguments);
	//wrtie for iframe.document or after page loading will cause insert js link invalid.
	//so attach a flag for the rewrite code to notice the server to insert the js file into the rewrite code.
	var cacheFlg = CACHE_FLAG_NONE;
	if(
		(SF_TYPE_DOCUMENT == SF_func_getobjtype(pDocObj)
		&&pDocObj !== document)//sometimes like eas5.0 in it's htc code,document != window.document
		|| SF_IsPageLoaded == true
		|| /javascript:/.test(pDocObj.location.href)
		|| /about:/i.test(pDocObj.location.href)
	)
	{
		cacheFlg = CACHE_FLAG_DOCUMENT_REPLACE;
	}
	
	
	SF_FUNC_cache_object(pDocObj,SF_FLS_WRITE,SF_FUNC_UnEscape(inText),cacheFlg);
	return true;
}

function SF_FUNC_REPLACE_WINDOW_SHELP_FUNCTION(winobj,istopobj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_REPLACE_WINDOW_SHELP_FUNCTION",arguments);
	if (!winobj || SF_func_isundefined(typeof winobj["showHelp"]) || SF_func_checkfunctionisloaded(winobj["open"])==1)
	{
		return;
	}
	winobj.SF_ORIFUNC_SHELP=winobj["showHelp"];
	winobj["showHelp"] = function SF_FUNC_SHELP(pageUrl)
	{
		var transedUrl = pageUrl;
		if (winobj.document.SF_g_conofcurloc != null);
		{
			transedUrl = SF_FUNC_transhttpurl(pageUrl,1,winobj.document.SF_g_conofcurloc);
		}
		try
		{
			try{
				return  winobj.SF_ORIFUNC_SHELP(transedUrl);
			}catch(e){
				SF_FUNC_Debug(e.message);
				return null;
			}
		}
		catch (e)
		{SF_FUNC_Debug(e.message);}
	}
}




function SF_FUNC_check_ispda()
{
	SF_FUNC_RoutineTrace("SF_FUNC_check_ispda",arguments);
	var agent = navigator.userAgent; 
	if(agent.indexOf("Windows CE") != -1 || agent.indexOf("Symbian") != -1  || agent.indexOf("Serials") != -1
		|| agent.indexOf("UIQ") != -1 || agent.indexOf("S60") != -1 )
	{
		return true;
	}
	return false;  
}


function SF_func_init_docvar(pDoc)
{
	SF_FUNC_RoutineTrace("SF_func_init_docvar",arguments);
	var winlocation = SF_FUNC_GetLocation_Script_Compatible();
	var curloc = SF_func_parsewinlocation(winlocation);
	
	if (curloc == null)
	{
		return false;
	}
	SF_g_conofcurloc = pDoc.SF_g_conofcurloc =  curloc;
	var curpath = curloc.svpninfo["pathname"];
	
	var curhost = curloc.svpninfo["host"];
	var ioffset = curhost.indexOf(":");
	if (ioffset > 0)
	{
		curhost = curhost.SF_substring(0,ioffset);
	}
    
	pDoc.__sf_cookies__=[];
	pDoc.__sf_cookies__["path"] = curpath;
	pDoc.__sf_cookies__["domain"] = curhost;
	pDoc.__sf_cookies__["secure"] = (curloc.svpninfo["protocol"] == "https" ? 1 :0 );
	__SF_g_cookies__ = pDoc.__sf_cookies__;
	return true;
}
function SF_FUNC_WEB_SERVER_INIT()
{ 
	SF_FUNC_RoutineTrace("SF_FUNC_WEB_SERVER_INIT",arguments);
	SF_FUNC_Modify_BasicFunctions();
	SF_FUNC_INIT_GlobalVars();
	SF_FUNC_Custom_Function();
	if (window.SF_g_svpn_svc_flag == true)
	{
		return;
	}  
	window.document.tmpfunc_doc_write = window.document["write"];
	window.document.__sf_location__ = SF_FUNC_GetLocation_Script_Compatible();

	if(!SF_func_init_docvar(window.document))
	{
		window.SF_g_svpn_svc_flag =false;
		return;
	}
	SF_g_svpnurl = SF_FUNC_GetLocation_Script_Compatible();
	SF_g_svpnurl = SF_g_svpnurl.match(/^(https?\:\/\/[^\/]+)(\/)?/i)[1];
	window.SF_g_svpn_svc_flag = true;
	if(!SF_FUNC_check_ispda())
	{
        
		try{
			SF_FUNC_REPLACE_WINDOW_SHELP_FUNCTION(window);
		}catch(e){SF_FUNC_Debug(e.message);}
	}
	
    SF_func_settypeofthiswindow(window);
	var onloadFunc = function()
	{
		SF_FUNC_deal_xsl_attrset();
		//SF_FUNC_deal_xsl_object();
		SF_IsPageLoaded = true;
	};
	if(window.attachEvent)
		window.attachEvent('onload',onloadFunc);
	else 
		window.addEventListener('load',onloadFunc,false);
}

/** 
    *rewrite content in the server side.
    *param 
                    content :string  content need to be rewrited
                    type:integer content type,can be SF_REWRITE_TYPE_XXX
     *retvalue
                    false:rewrite err.
                    string:content rewrited
***/
function SF_FUNC_Svr_Rewrite(content,type/*,flag*/)
{
	SF_FUNC_RoutineTrace("SF_FUNC_Svr_Rewrite",arguments);
	if(typeof(content) != "string")
	     return content;
	if(type >= SF_REWRITE_TYPE_MAX)
        return false;
    
	var flag = CACHE_FLAG_NONE;
	var flagValue = OPTION_FLAG_NONE;
	if(arguments.length >=3 && typeof(arguments[2])=="number")
		flag = arguments[2];
	
	var postLen = 0;
	var postdata = "";
	if(flag & CACHE_FLAG_DOCUMENT_REPLACE)
	{
		flagValue += OPTION_FLAG_DOCUMENT_REPLACE;
		//Just Use this flag once,so the same will insert svpn_websvc_functions.js once.
		SF_IsPageLoaded = false;
	}
	else if(flag &  CACHE_FLAG_POPUP_REPLACE)
	{
		flagValue += OPTION_FLAG_POPUP_REPLACE;
	}
	
	if(type == SF_REWRITE_TYPE_HTML && !(flag & CACHE_FLAG_DOCUMENT_REPLACE) )
	{
		if(
			!(/<(object|embed|applet|style|script)\s*/i.test(content) 
			|| /<\w+\b[^>]*((href)|(src)|(dynsrc)|(lowsrc)|(action)|(background)|(codebase)|(cite)|(data)|(profile)|(archive)|(on\w+))\s*=(.|\s)/i.test(content))
		)
			return content;
	}
	
	if(type == SF_REWRITE_TYPE_JS)
    {
		if(!content.match(SF_JS_KEYWORD_ExpObj))//if there is no key word in the js code,no need to rewrite.
			return content;
		postdata = SF_REWRITE_POST_CONTENT +"=" + "<script language='javascript'>" + content + "<\/script>"; 
    }
    else if(type == SF_REWRITE_TYPE_VBS)
    {
        postdata = SF_REWRITE_POST_CONTENT +"=" + "<script language='vbscript'>" + content + "<\/script>"; 
    }
	else if(type == SF_REWRITE_TYPE_CSS)
	{
		postdata = SF_REWRITE_POST_CONTENT +"=" + "<style>" + content + "<\/style>"; 
	}
    else
        postdata = SF_REWRITE_POST_CONTENT + "=" + content;
	
	postLen = SF_Util.getByteLen(postdata) - (SF_REWRITE_POST_CONTENT+'=').length;
    if(SF_REF_URL)
		postdata = SF_REWRITE_POST_REFURL + "=" + SF_REF_URL + SF_g_seperator + postdata;
	
	postdata = SF_REWRITE_POST_TYPE + "=" + (type+flagValue) + SF_g_seperator + postdata;
    postdata = SF_REWRITE_POST_LEN + "=" + postLen + SF_g_seperator + postdata;
	var cacheInfo;
	if(cacheInfo = SF_FUNC_GetRewriteCache(content,type,flag))
		return cacheInfo;
	
	var reWritedTxt = SF_func_post_http(SF_REWRITE_ADDR,postdata);
	if(SF_REWRITE_TYPE_JS==type || SF_REWRITE_TYPE_VBS==type || SF_REWRITE_TYPE_CSS==type)
	{
		if(type == SF_REWRITE_TYPE_JS)
		{
			reWritedTxt = reWritedTxt.replace(/\n?;?if\(typeof\(SF_FUNC_cache_flush\)=='function'\)SF_FUNC_cache_flush\(\);\s*<\/script>\s*$/,"<\/script>").replace(/<script language='javascript'>([\s\S]+?)<\/script>\s*$/,'$1');
		}
		else if(type == SF_REWRITE_TYPE_VBS)
		{
			reWritedTxt = reWritedTxt.replace(/\n?if\sisobject\(SF_FUNC_cache_flush\)\sthen\sSF_FUNC_cache_flush\(\)\s*<\/script>\s*$/,"<\/script>").replace(/.*<script\slanguage='vbscript'>([\s\S]+?)<\/script>\s*$/,'$1');
		}
		else if(type == SF_REWRITE_TYPE_CSS)
			reWritedTxt = reWritedTxt.replace(/.*<style>([\s\S]+?)<\/style>/,'$1');
	}
	
	SF_FUNC_CacheRewrite(content,type,flag,reWritedTxt);
	return reWritedTxt;
}


/** 
    *rewrite SF_FUNC_handle_xsl_attribute
    *param 
                    attrValue :string  content need to be rewrited
                    isNotEventAttr=true:whether the attr is a event.
     *retvalue:no retvalue
***/
function SF_FUNC_handle_xsl_attribute(attrValue/*,isNotEventAttr = true,execNow=true*/)
{
	SF_FUNC_RoutineTrace("SF_FUNC_handle_xsl_attribute",arguments);
	var isNotEventAttr = true;
	var execNow = true;
	var hasJSBegin = false;
    if(arguments.length > 1)
        isNotEventAttr = arguments[1];
	if(arguments.length == 3)
		execNow = arguments[2];
    attrValue = SF_FUNC_UnEscape(attrValue);
    var jsPrefixReg = /^\s*javascript:(.+)/i;
    if(!isNotEventAttr)
    {
		var reWritedTxt = SF_FUNC_Svr_Rewrite(attrValue,SF_REWRITE_TYPE_JS);
        if(execNow)
			eval(reWritedTxt);
    }
    else if(isNotEventAttr && jsPrefixReg.test(attrValue))
    {
		hasJSBegin = true;
        var postdata = attrValue.replace(jsPrefixReg,"$1");
        var reWritedTxt = SF_FUNC_Svr_Rewrite(postdata,SF_REWRITE_TYPE_JS);
        if(execNow)
			eval(reWritedTxt);
    }
    else
    {
        reWritedTxt = SF_FUNC_transhttpurl(attrValue,1,SF_g_conofcurloc);
        if(execNow)
			window.location = reWritedTxt;
    }
	if(!execNow)
	{
		if(hasJSBegin)
			reWritedTxt = "javascript:" + reWritedTxt;
		return reWritedTxt;
	}
}

/** 
    *Exec specified fun with given args;
    *param 
                    funName(string) :function name
                    Args(iarray) :args
     *retvalue:no retvalue
***/
function SF_FUNC_ExecFun(funName,Args)
{
	SF_FUNC_RoutineTrace("SF_FUNC_ExecFun",arguments);
    var argStr= "";
    for(var i = 0; i < Args.length; i++)
    {
        paramValue = typeof(Args[i])=="string" ? "'" + Args[i].replace(/\\/g,"\\\\").replace(/"/g,'\\"').replace(/'/g,"\\'").replace(/\n/g,'\\n') + "'" : Args[i];
        argStr += paramValue + ",";
    }
    argStr = argStr.SF_substr(0,argStr.length - 1);
    //try{
		return window.eval(funName+"("+ argStr  +")");
    //}catch(e){alert(e.message);}
}

/** 
    *Replacement of ActiveXObject,identiy
    *param 
                    value[string]:id|name of object
                    type[integer]: can be SF_MARKE_ID|SF_MARKE_NAME ,marke type
     *retvalue:no retvalue
**/
function SF_FUNC_SangforActiveXObject(clsName)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SangforActiveXObject",arguments);
	//for bug 15566 use the same xmlhttpObj constructor
	if(/(MSXML\d\.XMLHttp(\.\d)?)|(Microsoft\.XMLHttp)/i.test(clsName) 
		&& SF_Util.hasXMLHttpRequest 
		&& typeof XMLHttpRequest != 'undefined'
	)
	{
		return SF_func_createxmlhttp(clsName);
	}

	try{
		var obj = null;
		if(arguments.length == 1)
			obj = new ActiveXObject(clsName);
		else if(arguments.length == 2)
			obj = new ActiveXObject(clsName,arguments[1]);
		else if(arguments.length == 3)
			obj = new ActiveXObject(clsName,arguments[1],arguments[2]);
		else 
			obj = SF_FUNC_Eval_Function(window,"ActiveXObject",arguments);
	}catch(e){
		SF_FUNC_Debug(e.message);
		throw e;
	}
    
	SF_FUNC_AddReWriteObjs(obj,clsName,SF_OBJECT_LABEL_OBJECT);
	return obj;
}
/*
push the obj to SF_ReWriteObjs;
*/
function SF_FUNC_AddReWriteObjs(obj,clsName,OBJECT_LABEL)
{
	SF_FUNC_RoutineTrace("SF_FUNC_AddReWriteObjs",arguments);
    if(typeof(SF_ReWriteObjs) == "object" )//save the object in array.
	{
		var isExists = false;
		for(var i=0;i<SF_ReWriteObjs.length;i++)
		{
			if(SF_ReWriteObjs[i][SF_REWRITE_INDEX_CLASSID]==clsName && SF_ReWriteObjs[i][SF_REWRITE_INDEX_OBJECT]==obj)
			{
				isExists = true;
				break;
			}
		}
		if(!isExists)
			SF_ReWriteObjs.push([obj,clsName,OBJECT_LABEL]);
	}
}

/** 
    *SF_FUNC_GetReWriteInfoByObj
    *param 
                    obj[object] : instance of object. 
     *retvalue:return true if instance is marked.or no.
**/
function SF_FUNC_GetReWriteInfoByObj(obj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GetReWriteInfoByObj",arguments);
    if(typeof(SF_ReWriteObjs) != "object")
        return null;
    for(var i=SF_ReWriteObjs.length;i--;)
        if(SF_ReWriteObjs[i][SF_REWRITE_INDEX_OBJECT] == obj)
            return SF_ReWriteObjs[i];
	if(SF_OBJECT_LABEL_UNKNOW != SF_FUNC_getObjLabelType(obj))
		return [obj,SF_FUNC_getClasID(obj),SF_FUNC_getObjLabelType(obj)];
	else
		return null;
}

/** 
    *SF_FUNC_GetReWriteConfig
    *param 
                    obj[object] : instance of object. 
     *retvalue:return rewrite object info specified by parameters.null if not find.
**/
function SF_FUNC_GetReWriteConfig(clasId,markFlag)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GetReWriteConfig",arguments);
	if(typeof(SF_ReWriteObjsConfig)!="object")
        return null;
    
	for(var i=SF_ReWriteObjsConfig.length;i--;)
	{
		if(SF_FUNC_compareClasId(clasId,SF_ReWriteObjsConfig[i][SF_REWRITE_CFG_CLASSID])
			&& SF_ReWriteObjsConfig[i][SF_REWRITE_CFG_MARK_FLAG]==markFlag
		)
			return SF_ReWriteObjsConfig[i];
	}
	
    return null;
}

/** 
    *SF_FUNC_GetReWriteConfigItem
    *param 
                    obj[object] : instance of object. 
                    matchName[string] : attrName or funName need to find. 
                    matchType[int] : SF_KEY_TYPE_FUNC|SF_KEY_TYPE_LEFT_RIGHT. now can not differ from SF_KEY_TYPE_LEFT to SF_KEY_TYPE_RIGHT
									 because console don't have details about these.
     *retvalue:return rewrite object info specified by parameters.null if not find.
**/
function SF_FUNC_GetReWriteConfigItem(OBJ,matchName,matchType)
{
		var reWriteInfo,objInfo;
		if(!(objInfo = SF_FUNC_GetReWriteInfoByObj(OBJ))//find object info
			|| !(reWriteInfo = SF_FUNC_GetReWriteConfig(objInfo[SF_REWRITE_INDEX_CLASSID],objInfo[SF_REWRITE_INDEX_MARK_FLAG]))//find object rewrite info;
			)
		{
			return null;
		}
		
		if(matchType == SF_KEY_TYPE_LEFT_RIGHT)
		{
			var reWriteAttrs = reWriteInfo[SF_REWRITE_CFG_ATTR];
			var attrExists = false;
			for(var i=reWriteAttrs.length;i--;)
			{
				if(reWriteAttrs[i] == matchName)
				{
					attrExists = true;
					break;
				}
			}
			
			if(attrExists)
				return reWriteAttrs[i];
		}
		else if(matchType == SF_KEY_TYPE_FUNC && !SF_FUNC_IsUndefined(reWriteInfo[SF_REWRITE_CFG_FUN][matchName]) )	
			return reWriteInfo[SF_REWRITE_CFG_FUN][matchName];
			
		return null;
}


/** 
    *SF_FUNC_MergeReWriteObjsConfig
    *param 
                    obj[object] : instance of object. 
     *retvalue:return rewrite object info specified by parameters.null if not find.
**/
function SF_FUNC_MergeReWriteObjsConfig(obj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_MergeReWriteObjsConfig",arguments);
	if(typeof(SF_ReWriteObjsConfig)!="object"
		|| typeof(obj)!="object"
	)
        return;
    
	//decode html style ascii char,like &#dd;
	obj[SF_REWRITE_CFG_CLASSID] = SF_FUNC_HtmlToChr(obj[SF_REWRITE_CFG_CLASSID]);
	
	var origObj;
	if((origObj = SF_FUNC_GetReWriteConfig(obj[SF_REWRITE_CFG_CLASSID],obj[SF_REWRITE_CFG_MARK_FLAG])) === null)
	{
		SF_ReWriteObjsConfig.push(obj);
		return;
	}
	
	var key1,key2;
	for (key1 in obj)
	{
		if(typeof(origObj[key1])=="undefined")
			origObj[key1] = obj[key1];
		else if(typeof(origObj[key1])=="object")
		{
			//merge obj.attr or others in future
			if(obj[key1] instanceof Array)
			{
				for(var i=obj[key1].length;i--;)
				{
					var entryExists = false;
					for(var j=origObj[key1].length;j--;)
					{
						if(origObj[key1][j]==obj[key1][i])
						{	
							entryExists = true;;
							break;
						}
					}
					if(!entryExists)
							origObj[key1].push(obj[key1][i]);
				}
			}
			else//merge obj.fun or others in future
			{
				for (key2 in obj[key1])
				{
					if(typeof(origObj[key1][key2])=="undefined")
						origObj[key1][key2]=obj[key1][key2]
				}
			}
				
		}
	}
}


/** 
    *get label type of a object instance
    *param 
                    obj[object] : instance of object. 
     *retvalue[integer]:SF_OBJECT_LABEL_EMBED|SF_OBJECT_LABEL_OBJECT|SF_OBJECT_LABEL_APPLET
**/
function SF_FUNC_getObjLabelType(Obj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_getObjLabelType",arguments);
    if(Obj == null)
        return null;
    var ObjType = {
                    "EMBED":SF_OBJECT_LABEL_EMBED,
                    "OBJECT":SF_OBJECT_LABEL_OBJECT,
                    "APPLET":SF_OBJECT_LABEL_APPLET
                  }
    var labelType = SF_OBJECT_LABEL_UNKNOW;
	try{
		
		if(typeof(Obj.tagName) != 'string')
			return 	SF_OBJECT_LABEL_UNKNOW;
		var labelName = Obj.tagName.toUpperCase();
		labelType = (SF_FUNC_IsUndefined(ObjType[labelName])?SF_OBJECT_LABEL_UNKNOW:ObjType[labelName]);
	}
	catch(e){SF_FUNC_Debug(e.message);}
	
	if(labelType>=SF_OBJECT_LABEL_OBJECT && labelType<=SF_OBJECT_LABEL_UNKNOW)
		return labelType;
	else 
		return SF_OBJECT_LABEL_UNKNOW;
}

/** 
    *Compare classId
    *param 
                    classId[String] : classId to be compared 
                    rule[String] : rule by console config
     *retvalue[integer]:
**/
function SF_FUNC_compareClasId(classId,rule)
{
	SF_FUNC_RoutineTrace("SF_FUNC_compareClasId",arguments);
	if(typeof(classId) !='string'
		|| typeof(rule) !='string'
	)
		return false;
	
	function SF_FuzzyMatch(classId,rule)
	{
		var rulePieces = rule.split('*');
		if(rulePieces.length>1)//rule contains * then begain fuzzy match.
		{
			var offset = 0;
			var unMatch = false;
			for(var i=0;i<rulePieces.length;i++)
			{
				if((offset = classId.indexOf(rulePieces[i],offset)) == -1)
					return false;
				
				offset += rulePieces[i].length;
			}
			if(rule.charAt(0)!='*' && classId.indexOf(rulePieces[0]) !==0 )//try to match xxx*yyy with tttxxxyyy; 
				return false;
				
			if(rule.charAt(rule.length-1)=='*' 
				|| classId.SF_substr(offset).length == 0)//final part like yyy in xxx*yyy need to 
			return true;
		}
		return false;
	}
	
	var isFlash = /\.swf/i.test(classId);
	
	if(isFlash)
	{
		var qRet;
		if(qRet = classId.match(/(\?.+$)/))//restore querystring parameters have been rewrited.
		{
			var queryArr=qRet[1].split("&");
			for(var i=0;i<queryArr.length;i++)
			{
				var parArr = queryArr[i].split('=');
				parArr[1] = SF_FUNC_RemoveCookieFlag(parArr[1]);//replace cookie flag.
				if(parArr && parArr.length==2 && SF_func_checkurliftransed(parArr[1]))
				{
					parArr[1] = SF_FUNC_revertmyurl(parArr[1]).replace(/\/$/,'');
					queryArr[i] = parArr.join('=');
				}
			}
			classId = classId.replace(/\?(.+$)/,queryArr.join('&'));
		}
	}
		
	if(rule == classId)
		return true;
	else if(rule.indexOf('*') != -1)
	{
		if(SF_FuzzyMatch(classId,rule))
			return true;
		else if(!isFlash)
			return false;
	}
	
	if(isFlash)//if it's flash.
	{
		var curLoc = SF_FUNC_parsemyurl(SF_FUNC_GetLocation_Script_Compatible());//if flash does not match here,pad with the host and fileptah,try again.
		if(classId.charAt(0)=='/')
			classId = curLoc["sProtocol"] + "://" + curLoc["sHost"] + classId;
		else
			classId = curLoc["sProtocol"] + "://" + curLoc["sHost"] + curLoc["sFile"].replace(/[^\/\\]+$/,'') + classId;
		
		if(rule.indexOf('*') != -1)
			return SF_FuzzyMatch(classId,rule);
		else if((classId.length > rule.length)
				&&(classId.SF_substr(classId.length-rule.length).toLowerCase()==rule.toLowerCase())//need to match the end of the clasid complete.
				&&(classId.SF_substr(classId.length-rule.length).match(/[\/\\]?[^\/\\]+\.swf/) && !classId.SF_substr(0,classId.length-rule.length).match(/\.swf/))
				)
			return true;
	}
	
	return false;
}

/** 
    *get param value from object or applet innerHTML
    *param 
                    obj[object] : instance of object. 
     *retvalue[integer]:
**/
function SF_FUNC_getParamValue(obj,name)
{
	SF_FUNC_RoutineTrace("SF_FUNC_getParamValue",arguments);
    for(var i=obj.childNodes.length;i--;)
	{
		if(!SF_FUNC_IsUndefined(obj.childNodes[i]["tagName"])
			&&obj.childNodes[i].tagName.toLowerCase() =='param'
			&&obj.childNodes[i].getAttribute("name")
			&&obj.childNodes[i].getAttribute("name").toLowerCase()== name.toLowerCase())
			return SF_func_trim(obj.childNodes[i].getAttribute("value"));
	}
	return null;
}


/** 
    *get ClasId of a object instance
    *param 
                    obj[object] : instance of object. 
     *retvalue[integer]:
**/
function SF_FUNC_getClasID(Obj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_getClasID",arguments);
    if(Obj == null)
        return null;
    var labelType = SF_FUNC_getObjLabelType(Obj);
	var clasId = null;
    switch(labelType)
    {
        case SF_OBJECT_LABEL_EMBED:
			clasId = Obj.getAttribute("src") ? Obj.getAttribute("src") : Obj.getAttribute("code");
			break;
        case SF_OBJECT_LABEL_OBJECT:
		{
			var paramNames =["code","src","movie"];
			for(var i=0;i<paramNames.length;i++)
			{
				if(clasId = SF_FUNC_getParamValue(Obj,paramNames[i]))
					break;
			}
			
			if(!clasId)
			{
				var attrNames =["data","classid","code"];
				for(var i=0;i<attrNames.length;i++)
				{
					if(clasId = Obj.getAttribute(attrNames[i]))
						break;
				}
			}
			break;
		}
		case SF_OBJECT_LABEL_ACTIVEX:
			clasId = Obj.getAttribute("classid");
			break;
        case SF_OBJECT_LABEL_APPLET:
		{
			if(SF_FUNC_getParamValue(Obj,"code"))
				clasId = SF_FUNC_getParamValue(Obj,"code");
			else if(Obj.getAttribute("code"))
				clasId = Obj.getAttribute("code");
			break;
		}
		default:return null;
    }
	
	if(clasId && /^(java)|(clsid)\:/i.test(clasId))
		clasId = clasId.replace(/^((java)|(clsid))\:/i,"");
	else 
		clasId = SF_FUNC_revertmyurl(clasId);
	
	return clasId;
}
/** 
    *Add new OBj rewrite configuration to SF_ReWriteObjsConfig,Used to dispatch dynamically by the server.
    *param 
                    reWriteInfoCfg[object] :  custon rewrite info of object. {"clsid"{...}} ,this in only one object rewrite config in this object.
     *retvalue:no return value
**/
function SF_FUNC_AddReWriteObjsConfig(reWriteInfoCfg)
{
	SF_FUNC_RoutineTrace("SF_FUNC_AddReWriteObjsConfig",arguments);
	if(typeof(reWriteInfoCfg) =='string')
		eval("reWriteInfoCfg = [" + SF_Encrypt.base64decode(reWriteInfoCfg) + "]");
	
	if(typeof(reWriteInfoCfg) !='object')
		return SF_ReWriteObjsConfig;
	for(var i=reWriteInfoCfg.length;i--;)
	{
		SF_FUNC_MergeReWriteObjsConfig(reWriteInfoCfg[i]);
	}
	return SF_ReWriteObjsConfig;
}

var SF_xslscript_collect={};
SF_xslscript_collect.Eval=function(code)
{
	SF_FUNC_RoutineTrace("SF_xslscript_collect.Eval",arguments);
	if(!!(window.attachEvent && !window.opera))
        return execScript(code);
    else
        return window.eval(code);
}

/** 
    *handle script created by xsl
    *param 
                    scriptTxt[String] :  script code
                    scriptType[int] :  SF_REWRITE_TYPE_JS | SF_REWRITE_TYPE_VBS
     *retvalue:no return value
**/
function SF_FUNC_handle_xslscript(scriptTxt,scriptType)
{
	SF_FUNC_RoutineTrace("SF_FUNC_handle_xslscript",arguments);
	var reWritedTxt = SF_FUNC_Svr_Rewrite(scriptTxt,scriptType);
	SF_xslscript_collect.Eval(reWritedTxt);
	SF_FUNC_cache_flush();
}

/** 
    *handle eval function,rewrite the parameter of eval.
    *param 
                    scriptTxt[String] :  script code
                    scriptType[int] :  SF_REWRITE_TYPE_JS | SF_REWRITE_TYPE_VBS
     *retvalue:no return value
**/
function SF_FUNC_Handle_Eval(expression)
{
	SF_FUNC_RoutineTrace("SF_FUNC_Handle_Eval",arguments);
	SF_FUNC_cache_flush();
	return SF_FUNC_Svr_Rewrite(expression,SF_REWRITE_TYPE_JS);
}

/** 
    *handle ececScript function,rewrite the parameter of execScript.
    *param 
                    scriptTxt[String] :  script code
                    scriptType[int] :  SF_REWRITE_TYPE_JS | SF_REWRITE_TYPE_VBS
     *retvalue:no return value
**/
function SF_FUNC_Handle_ExecScript(expression)
{
	SF_FUNC_RoutineTrace("SF_FUNC_Handle_ExecScript",arguments);
	SF_FUNC_cache_flush();
	
	var scriptLan = SF_REWRITE_TYPE_JS;
	if(arguments.length>1 && typeof(arguments[1])=="string" && /vb/i.test(arguments[1]))
		scriptLan = SF_REWRITE_TYPE_VBS;
	
	return SF_FUNC_Svr_Rewrite(expression,scriptLan);
}

/** 
    *check url is fake or not
    *param 
                    url[String] : url to be checked.
    *retvalue[boolean]:true|false
**/
/*WARNING:NOT USE NOW.
function SF_FUNC_IsFakeUrl(url)
{
	SF_FUNC_RoutineTrace("SF_FUNC_IsFakeUrl",arguments);
	var fakeUrlReg = new RegExp(SF_FAKEURL_FLAG,"i");
	return fakeUrlReg.test(url);
}
*/


/** 
    *check current site is fake or not
    *param none;
    *retvalue[boolean]:true|false
**/
function SF_FUNC_IsFake()
{
	SF_FUNC_RoutineTrace("SF_FUNC_IsFake",arguments);
	return SF_Util.isFake;
}

/** 
var flashObj = {
	"object":null,
	"method":"write|writeln|innerHTML",
	"cacheData":""
};
    *Flush the cached elements
    *param none;
	*param isSrc true / false|null|undefined
    *retvalue:none;
**/
function SF_FUNC_cache_flush(isSrc)
{
	SF_FUNC_RoutineTrace("SF_FUNC_cache_flush",arguments);
	var flahObjs = SF_g_flahObjs;
	for(var i=0;i< SF_g_flahObjs.length;i++)
	{
		var curflashobj = flahObjs[i];
		var method = curflashobj["method"];
		var cache_object = curflashobj["object"];
		
		if(!(curflashobj && cache_object && curflashobj["content"]))
			continue;
		
		//if context is in a setTimeout or setInteral
		//document.write will clear the variables and rebuild the page.
		//So generally,there will be no doc.write in this case.
		if(SF_IsTimeOut>0 
			//&& !(flahObjs[i]["flag"] & CACHE_FLAG_DOCUMENT_REPLACE)
			&& (method ==SF_FLS_WRITE || method==SF_FLS_WRITELN ) 
			&& cache_object == window.document
		) 
			continue;
		
		//Rewrite String from the server rewriter
		if(curflashobj["use"] === true)
			continue;
		else
			curflashobj["use"] = true;
		
		var reWritedContent = SF_FUNC_Svr_Rewrite(curflashobj["content"],SF_REWRITE_TYPE_HTML,curflashobj["flag"]);
		
		if(!curflashobj)//In ff,during the block of SYNC AJAX,script src= may change every variable dynamically.
			continue;
		
		try{
			curflashobj["content"] = "";
			curflashobj["use"] = false;
			SF_FUNC_cache_del(cache_object,method);
		}catch(e){
			SF_FUNC_Debug(e.message);
		}
		
		if(!cache_object)//In ff,during the block of SYNC AJAX,script src= may change every variable dynamically.
			continue;
			
	    switch(method)//curflashobj was not deleted by SF_FUNC_cache_del,actually just remove from the it.
		{
			case SF_FLS_WRITE:
			case SF_FLS_WRITELN:
			{
				cache_object.write(reWritedContent);
				break;
			}
			case SF_FLS_INNERHTML:
			{
				curflashobj["object"].innerHTML = reWritedContent;
				break;
			}
		}
	}
	if(isSrc){
		return '';
	}else{
		return undefined;
	}
}

/** 
    *Find a position where a complete element end.
	*example:<a href="url">test</a><a href= return the end position of <a href="url">test</a>
    *param
			str[String]
	*retvalue:position index of the index;
**/
function SF_FUNC_cache_parseHtml(str)
{
	SF_FUNC_RoutineTrace("SF_FUNC_cache_parseHtml",arguments);
	//var regExp =  /<[a-z]+\s+((id)|(name))=[^\s>]+[^>]*\/?>([^<>]+<\/[^>]+>)?/i;
	var regExp =  /(<\w+[^<>]*\/>)|(<\/\w+>)|(<input[^<>]*\/?>)|(<link[^>]+>)/im;
	var lastIndex;
	var index = 0 ;
	var ret;
	while(ret=str.match(regExp))
	{
		lastIndex = ret[0].length + ret.index;
		index += lastIndex;
		str = str.SF_substring(lastIndex);
	}
	return index;
}

/** 
    *Flush the cached elements
    *param
			obj[object]
			method[integer]:SF_FLS_WRITE|SF_FLS_INNERHTML
    *retvalue:none;
**/
function SF_FUNC_cache_flush_single(obj,method)
{
	SF_FUNC_RoutineTrace("SF_FUNC_cache_flush_single",arguments);
	if(!obj || !SF_g_flahObjs)
		return;
	if(method == SF_FLS_ALL)
	{
		for(var i=SF_FLS_WRITE;i<SF_FLS_ALL;i++)
			SF_FUNC_cache_flush_single(obj,i);
		return;
	}
	var flahObjs = SF_g_flahObjs;
	for(var i=0;i< flahObjs.length;i++)
	{
		var curflashobj = flahObjs[i];
		if(curflashobj && curflashobj["object"] == obj && curflashobj["method"] == method)
		{
			var domObjFlag;
			if(domObjFlag = SF_FUNC_cache_parseHtml(curflashobj["content"]))
			{
				//if context is in a setTimeout or setInteral
				//document.write will clear the variables and rebuild the page.
				//So generally,there will be no doc.write in this case.
				if(SF_IsTimeOut>0 
					//&& !(curflashobj["flag"] & CACHE_FLAG_DOCUMENT_REPLACE)
					&&(curflashobj["method"]==SF_FLS_WRITE || curflashobj["method"]==SF_FLS_WRITELN ) 
					&& curflashobj["object"]==window.document
				) 
				continue;
				
				if(curflashobj["use"] === true)//prevent from other dynamic call like "script src" will change the SF_g_flahObjs
					continue;
				else
					curflashobj["use"] = true;
					
				var method = curflashobj["method"];
				var cache_object = curflashobj["object"];
				var reWritedContent = SF_FUNC_Svr_Rewrite(curflashobj["content"].SF_substring(0,domObjFlag),SF_REWRITE_TYPE_HTML,curflashobj["flag"]);
				
				if(!curflashobj)//In ff,during the block of SYNC AJAX,script src= may change every variable dynamically.
					continue;
					
				try{
					curflashobj["use"] = false;
					curflashobj["content"] = curflashobj["content"].SF_substring(domObjFlag);
					if(SF_func_trim(curflashobj["content"])=="")
						SF_FUNC_cache_del(curflashobj["object"],curflashobj["method"]);
				}catch(e){
					SF_FUNC_Debug(e.message);
				}
				
				if(!cache_object)//In ff,during the block of SYNC AJAX,script src= may change every variable dynamically.
					continue;
			
				switch(method)
				{
					case SF_FLS_WRITE:
					case SF_FLS_WRITELN:
					{
						cache_object.write(reWritedContent);
						break;
					}
					case SF_FLS_INNERHTML:
					{
						cache_object.innerHTML = reWritedContent;
						break;
					}
				}
			}
			return;
		}
	}
	
	
}


/** 
    *Cache a Object;
    *param
			obj[object]
			method[integer]:SF_FLS_WRITE|SF_FLS_INNERHTML
			content:data need to be output.
			flag[option=CACHE_FLAG_NONE|CACHE_FLAG_DOCUMENT_REPLACE]:some mark for the cache info.
				0:no mark info;
				1<<0:need to insert public rewrite js file
				1<<1:...reserverd.
    *retvalue:none;
**/
function SF_FUNC_cache_object(obj,method,content/*,flag=null*/)
{
	SF_FUNC_RoutineTrace("SF_FUNC_cache_object",arguments);
	if(SF_g_flahObjs)
	{
		var flag = CACHE_FLAG_NONE;
		if(arguments.length>3 && typeof(arguments[3])=='number')
			flag = arguments[3];
		var i;
		var objCacheConent;
		for(i=0;i< SF_g_flahObjs.length;i++)
		{
			if(SF_g_flahObjs[i] && SF_g_flahObjs[i]["object"] == obj && SF_g_flahObjs[i]["method"] == method)
			{
				if(SF_g_flahObjs[i]["content"])
					SF_g_flahObjs[i]["content"] += content;
				else
					SF_g_flahObjs[i]["content"] = content;
				objCacheConent = SF_g_flahObjs[i]["content"];
				break;
			}
		}
		if(i == SF_g_flahObjs.length)
		{
			SF_g_flahObjs.push({
				"object":obj,
				"method":method,
				"content":content,
				"flag":flag
			});
			objCacheConent = content;
		}
		if(SF_FUNC_cache_hasImmediateObjects(objCacheConent))
			SF_FUNC_cache_flush_single(obj,method);
	}
}


/** 
    *Find If there is some elements need to be flash immediately.
	*<script src="xx.js"><\/script>
	*<script>newDefineFun function()...<\/script>
	*<link>...<\/link>
	*param
			content[string]:cache to be checked.
	*retvalue[boolean]:true | false
**/
function SF_FUNC_cache_hasImmediateObjects(content)
{
	SF_FUNC_RoutineTrace("SF_FUNC_cache_hasImmediateObjects",arguments);
	//as some unstandard code use id or name to control a HTML element.
	//so when cached string has something match a complete node that with name or id.flash it.
	var ret;
	var checkClosureTag = false;
	if((ret = content.match(/(<(\w+)[^<>]*\b(name|id)=[^<>]+[^\/]>[\s\S]*<\/\2>)|(<(\w+)[^<>]*\b(name|id)\s*=\s*[^<>]+\/>)|(<input[^<>]*\b(name|id)=[^<>]+\/?>)/gim))
		&& !ret[0].match(/^<(param|tr|td)[^<>]*\b(name|id)=/i))
	{
		checkClosureTag = true;
	}
	else if(ret = content.match(/<script[^>]*?>[\s\S]*?<\/script>|<html[^>]*?>[\s\S]*?<\/html>|<style[^>]*?>[\s\S]*?<\/style>|<iframe[^>]*?>[\s\S]*?<\/iframe>|<link[^>]+>/i))
	{
		//else if(ret = content.match(/(<script[^>]*>[\s\S]*<\/script>)|(<html[^>]*>[\s\S]*<\/html>)|(<style[^>]*>[\s\S]*<\/style>)|(<iframe[^>]*>[\s\S]*<\/iframe>)|(<link[^>]*>[.\n]*(<\/link>)?)/gim))
		checkClosureTag = true;
	}
	
	if(checkClosureTag)
	{
		if(/<html[^>]*>/i.test(content) && !/<html[^>]*>[\s\S]*?<\/html>/i.test(content)
		||(/<body[^>]*>/i.test(content) && !/<body[^>]*>[\s\S]*?<\/body>/i.test(content))
		||(/<script[^>]*>/i.test(content) && !/<script[^>]*>[\s\S]*?<\/script>/i.test(content))
		||(/<object[^>]*>/i.test(content) && !/<object[^>]*>[\s\S]*?<\/object>/i.test(content))
		||(/<table[^>]*>/i.test(content) && !/<table[^>]*>[\s\S]*?<\/table>/i.test(content))
		||(/<div[^>]*>/i.test(content) && !/<div[^>]*>[\s\S]*?<\/div>/i.test(content))
		)
			return false;
			
		return true;
	}
			
			
	return false;
}


/** 
    *Delete the cached object
    *param
			obj[object]
			method[integer]:SF_FLS_WRITE|SF_FLS_INNERHTML
    *retvalue:none;
**/
function SF_FUNC_cache_del(obj,method)
{
	SF_FUNC_RoutineTrace("SF_FUNC_cache_del",arguments);
	if(SF_g_flahObjs)
	{
		for(var i=0;i< SF_g_flahObjs.length;i++)
		{
			if(SF_g_flahObjs[i] && SF_g_flahObjs[i]["object"] == obj && SF_g_flahObjs[i]["method"] == method)
			{
				SF_g_flahObjs = SF_FUNC_array_remove(SF_g_flahObjs,i);
				return;
			}
		}
	}
}

/** 
    *Remove a array entry by index;
    *param
			arrObj[object]
			index[integer]:index of the entry;
    *retvalue:processed array;
**/
function SF_FUNC_array_remove(arrObj,index)
{
	SF_FUNC_RoutineTrace("SF_FUNC_array_remove",arguments);
	if(index > arrObj.length)
		return;
	for(var i=index;i<arrObj.length && (index != arrObj.length - 1);i++)
		arrObj[i] = arrObj[i + 1];
	arrObj.length--;
	return arrObj;
}


/** 
    *deal the elements,that attrs ares created by attribute-set
    *param none;
    *retvalue:none;
**/
function SF_FUNC_deal_xsl_attrset()
{
	SF_FUNC_RoutineTrace("SF_FUNC_deal_xsl_attrset",arguments);
	var SF_XSL_ATTRSET_FLAG = "SF_ATTR_SETS";
	var tags = ["a","input","button","img"];
	var links = ["img","href","src"];
	var events = ["onclick","onblur","ondblclick","onfocus","onkeydown","onkeypress","onkeyup"];
	var key;
	for(key in  tags)
	{
		var elements = document.getElementsByTagName(tags[key]);
        if (SF_FUNC_IsUndefined(elements))
            continue;
		for(var i=0;i<elements.length;i++)
		{
			if(elements[i].getAttribute(SF_XSL_ATTRSET_FLAG) !== "1" )
				continue;
			for(var j=0;j<links.length;j++)
			{
				var attValue = elements[i].getAttribute(links[j]);
				if(elements[i].tagName.toLowerCase() == "img")
				{
					if(links[j] != "src")
						continue;
					else
					{
						//url has querystring,it's & and other xml special char will be 
						attValue = SF_FUNC_UnEscapeXML(SF_FUNC_GetOuterHTML(elements[i]).replace(/.*src=['"]([^'"]*)['"].*/,"$1"));
					}
				}	
				if(elements[i].getAttribute(links[j]))
					elements[i].setAttribute(links[j], SF_FUNC_handle_xsl_attribute(attValue,true,false));
			}
			
			for(var j=0;j<events.length;j++)
			{
				if(elements[i].getAttribute(events[j]))
				{
					(function(){
					var eventHandle = elements[i].getAttribute(events[j]);
					if(eventHandle.constructor == Function)
					{
						eventHandle = eventHandle.toString().replace(/function\s+\w+\(\s*\)\s*{\s*(.*)\s*}.*/i,'$1');
						elements[i].setAttribute(events[j],function(){eval(SF_FUNC_handle_xsl_attribute(eventHandle,false,false));});
					}
					else if(eventHandle.constructor == String)
						elements[i].setAttribute(events[j],SF_FUNC_handle_xsl_attribute(elements[i].getAttribute(events[j]),false,false));
					})();
				}
			}
		}
	}
}


/** 
    *deal the objects created by xsl
    *param none;
    *retvalue:none;
**/
function SF_FUNC_deal_xsl_object()
{
	SF_FUNC_RoutineTrace("SF_FUNC_deal_xsl_object",arguments);
	var SF_XSL_OBJECT_FLAG = "sf_objrw_object";
	var elements = document.getElementsByTagName("div");
    if (SF_FUNC_IsUndefined(elements))
        return;
	for(var i=0;i<elements.length;i++)
	{
		var eleTagName = elements[i].getAttribute(SF_XSL_OBJECT_FLAG);
		if(!eleTagName)
			continue;
		
		eleTagName = eleTagName.toLowerCase();
		if(eleTagName!="object" &&eleTagName!="embed" && eleTagName !="applet")
			continue;
		
		var htmlCode = SF_FUNC_UnEscapeXML(SF_FUNC_GetOuterHTML(elements[i]));
		var matchRet = htmlCode.match(/<(div)(\s+[^<>]*)sf_objrw_object=['"](\w+)['"]([^<>]*>)/i);
		if(!matchRet)
			continue;
		htmlCode = "<" + matchRet[3] + matchRet[2] + matchRet[4];
		if(!/\/>/.test(matchRet[4]))
		{	
			for(var j=0;j<elements[i].childNodes.length;j++)//prepare object's subnodes.
			{
				var subOuterHTML = SF_FUNC_GetOuterHTML(elements[i].childNodes[j]);
				if(SF_FUNC_IsUndefined(elements[i].childNodes[j].getAttribute))
					continue;
					
				var subHtmlCode = SF_FUNC_UnEscapeXML(subOuterHTML);
				var subTagName = elements[i].childNodes[j].getAttribute(SF_XSL_OBJECT_FLAG);
				if(!subTagName)
					htmlCode += subHtmlCode;
				else
				{
					subHtmlCode = subHtmlCode.replace(/<div(\s+[^<>]*)sf_objrw_object=['"](\w+)['"]([^<>]*>).*/i,"<$2 $1 $3").replace(/([^\/])>$/,"$1\/>");
					htmlCode += subHtmlCode;
				}
			}
			htmlCode += "\n<\/" + matchRet[3] + ">\n";
		}
		elements[i].id="SF_" + elements[i].id;//htmlCode is the clone of elements[i],id should be unique.
		elements[i].innerHTML = SF_FUNC_Svr_Rewrite(htmlCode,SF_REWRITE_TYPE_HTML);
	}
}


var SF_Encrypt = {
	// private property
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
 
	// public method for encoding
	base64encode : function (input,hostonly) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;
		
		var port = null;
		if(hostonly && input.indexOf(':') != -1)
		{
			var ret  = input.match(/^([^:]+)(.*)$/);
			input = ret[1];
			port = ret[2];
		}
			
		input = SF_Encrypt._utf8_encode(input);
 
		while (i < input.length) {
 
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
 
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
 
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
 
			output = output +
			this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
		}
		
		if(port)
			output += port;
		
		return output;
	},
 
	// public method for decoding
	base64decode : function (input,hostonly) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
		
		var port = null;
		if(hostonly && input.indexOf(':') != -1)
		{
			var ret  = input.match(/^([^:]+)(.*)$/);
			input = ret[1];
			port = ret[2];
		}
			
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
 
		while (i < input.length) {
 
			enc1 = this._keyStr.indexOf(input.charAt(i++));
			enc2 = this._keyStr.indexOf(input.charAt(i++));
			enc3 = this._keyStr.indexOf(input.charAt(i++));
			enc4 = this._keyStr.indexOf(input.charAt(i++));
 
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
 
			output = output + String.fromCharCode(chr1);
 
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
 
		}
 
		output = SF_Encrypt._utf8_decode(output);
 
		if(port)
			output += port;
			
		return output;
 
	},
 
	// private method for UTF-8 encoding
	_utf8_encode : function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
 
		for (var n = 0; n < string.length; n++) {
 
			var c = string.charCodeAt(n);
 
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
		}
		return utftext;
	},
 
	// private method for UTF-8 decoding
	_utf8_decode : function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
 
		while ( i < utftext.length ) {
 
			c = utftext.charCodeAt(i);
 
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
 
		}
 
		return string;
	},
	crc32 : function( str ) 
	{
		str =  SF_Encrypt._utf8_encode(str);
		var table = "00000000 77073096 EE0E612C 990951BA 076DC419 706AF48F E963A535 9E6495A3 0EDB8832 79DCB8A4 E0D5E91E 97D2D988 09B64C2B 7EB17CBD E7B82D07 90BF1D91 1DB71064 6AB020F2 F3B97148 84BE41DE 1ADAD47D 6DDDE4EB F4D4B551 83D385C7 136C9856 646BA8C0 FD62F97A 8A65C9EC 14015C4F 63066CD9 FA0F3D63 8D080DF5 3B6E20C8 4C69105E D56041E4 A2677172 3C03E4D1 4B04D447 D20D85FD A50AB56B 35B5A8FA 42B2986C DBBBC9D6 ACBCF940 32D86CE3 45DF5C75 DCD60DCF ABD13D59 26D930AC 51DE003A C8D75180 BFD06116 21B4F4B5 56B3C423 CFBA9599 B8BDA50F 2802B89E 5F058808 C60CD9B2 B10BE924 2F6F7C87 58684C11 C1611DAB B6662D3D 76DC4190 01DB7106 98D220BC EFD5102A 71B18589 06B6B51F 9FBFE4A5 E8B8D433 7807C9A2 0F00F934 9609A88E E10E9818 7F6A0DBB 086D3D2D 91646C97 E6635C01 6B6B51F4 1C6C6162 856530D8 F262004E 6C0695ED 1B01A57B 8208F4C1 F50FC457 65B0D9C6 12B7E950 8BBEB8EA FCB9887C 62DD1DDF 15DA2D49 8CD37CF3 FBD44C65 4DB26158 3AB551CE A3BC0074 D4BB30E2 4ADFA541 3DD895D7 A4D1C46D D3D6F4FB 4369E96A 346ED9FC AD678846 DA60B8D0 44042D73 33031DE5 AA0A4C5F DD0D7CC9 5005713C 270241AA BE0B1010 C90C2086 5768B525 206F85B3 B966D409 CE61E49F 5EDEF90E 29D9C998 B0D09822 C7D7A8B4 59B33D17 2EB40D81 B7BD5C3B C0BA6CAD EDB88320 9ABFB3B6 03B6E20C 74B1D29A EAD54739 9DD277AF 04DB2615 73DC1683 E3630B12 94643B84 0D6D6A3E 7A6A5AA8 E40ECF0B 9309FF9D 0A00AE27 7D079EB1 F00F9344 8708A3D2 1E01F268 6906C2FE F762575D 806567CB 196C3671 6E6B06E7 FED41B76 89D32BE0 10DA7A5A 67DD4ACC F9B9DF6F 8EBEEFF9 17B7BE43 60B08ED5 D6D6A3E8 A1D1937E 38D8C2C4 4FDFF252 D1BB67F1 A6BC5767 3FB506DD 48B2364B D80D2BDA AF0A1B4C 36034AF6 41047A60 DF60EFC3 A867DF55 316E8EEF 4669BE79 CB61B38C BC66831A 256FD2A0 5268E236 CC0C7795 BB0B4703 220216B9 5505262F C5BA3BBE B2BD0B28 2BB45A92 5CB36A04 C2D7FFA7 B5D0CF31 2CD99E8B 5BDEAE1D 9B64C2B0 EC63F226 756AA39C 026D930A 9C0906A9 EB0E363F 72076785 05005713 95BF4A82 E2B87A14 7BB12BAE 0CB61B38 92D28E9B E5D5BE0D 7CDCEFB7 0BDBDF21 86D3D2D4 F1D4E242 68DDB3F8 1FDA836E 81BE16CD F6B9265B 6FB077E1 18B74777 88085AE6 FF0F6A70 66063BCA 11010B5C 8F659EFF F862AE69 616BFFD3 166CCF45 A00AE278 D70DD2EE 4E048354 3903B3C2 A7672661 D06016F7 4969474D 3E6E77DB AED16A4A D9D65ADC 40DF0B66 37D83BF0 A9BCAE53 DEBB9EC5 47B2CF7F 30B5FFE9 BDBDF21C CABAC28A 53B39330 24B4A3A6 BAD03605 CDD70693 54DE5729 23D967BF B3667A2E C4614AB8 5D681B02 2A6F2B94 B40BBE37 C30C8EA1 5A05DF1B 2D02EF8D";
		var crc = 0;
		var x = 0;
		var y = 0;
		
		crc = crc ^ (-1);
		for (var i = 0, iTop = str.length; i < iTop; i++) {
			y = ( crc ^ str.charCodeAt( i ) ) & 0xFF;
			x = "0x" + table.substr( y * 9, 8 );
			crc = ( crc >>> 8 ) ^ x;
		}
		
		return new String(crc ^ (-1));
	}
}

function SF_FUNC_UnEscape(inTxt)
{
	SF_FUNC_RoutineTrace("SF_FUNC_UnEscape",arguments);
	if(typeof(inTxt) == "string")
	{
		return inTxt.replace(/\$(quot|squot|rt|nl|gt|lt)\$/g,function(){
														switch(arguments[1])
															{
																case 'quot':return '"';
																case 'squot':return "'";
																case 'rt':return "\r";
																case 'nl':return "\n";
																case 'gt':return ">";
																case 'lt':return "<";
																default:return arguments[1];
															}
														});
	}
	else
		return inTxt;
}




function SF_FUNC_UnEscapeXML(inTxt)
{
	SF_FUNC_RoutineTrace("SF_FUNC_UnEscapeXML",arguments);
	if(typeof(inTxt) == "string")
		return inTxt.replace(/&lt;/g,'<').replace(/&gt;/g,">").replace(/&amp;/g,'&').replace(/&apos;/g,"'").replace(/&quot;/g,'"');
	else
		return inTxt;
}

function SF_FUNC_HtmlToChr(inTxt)
{
	SF_FUNC_RoutineTrace("SF_FUNC_HtmlToChr",arguments);
	if(typeof(inTxt) == "string")
		return inTxt.replace(/&#(\d+);/g,function(){return String.fromCharCode(arguments[1])});
	else
		return inTxt;
}

function SF_FUNC_InArray(entry,arr)
{
	SF_FUNC_RoutineTrace("SF_FUNC_InArray",arguments);
	if(!(arr && typeof(arr)=='object'))
		return false;
	
	for(var i=arr.length;i--;)
	{
		if(arr[i] == entry)
			return true;
	}
	
	return false;
}


function SF_FUNC_ChrToHtml(inTxt,filterChrs)
{
	SF_FUNC_RoutineTrace("SF_FUNC_ChrToHtml",arguments);
	if(!(filterChrs && typeof(filterChrs)=='object'))
		return inTxt;
	
	var outTxt="";
	for(var i=0;i<inTxt.length;i++)
	{
		if(SF_FUNC_InArray(inTxt.charAt(i),filterChrs))
			outTxt += "&#" + inTxt.charCodeAt(i).toString() + ";";
		else
			outTxt += inTxt.charAt(i);
	}
	return outTxt;
}


function SF_FUNC_Page_End()
{
	SF_FUNC_RoutineTrace("SF_FUNC_Page_End",arguments);
	SF_FUNC_cache_flush();
	SF_FUNC_deal_xsl_object();
	try{
		if(SF_FUNC_IsUndefined(document["body"]["onload"]))
			return ;
	}catch(e){
		SF_FUNC_Debug(e.message);
		return;
	}
	var old_onload = null;
	try{
		old_onload = document.body.onload;
		if(typeof(old_onload) != 'function')
		{
			old_onload = document.body.onload();
		}
	}catch(e){
		try{old_onload = document.body.onload();}catch(e){}
	}
	
	if(old_onload)
	{
		document.body.onload = function my_onload()
		{
			if(typeof(old_onload)=="function")
				old_onload();
			
			SF_IsPageLoaded = true;
		};
	}
}
	
/** 
    *save the basic function use by svpn_websc+function.js,
	*so if webrc page modify the functions will not effect out script.
    *param:none
    *retvalue:no retvalue
**/
	
function SF_FUNC_Modify_BasicFunctions()
{
	SF_FUNC_RoutineTrace("SF_FUNC_Modify_BasicFunctions",arguments);
	if(!String.prototype.SF_substr)
		String.prototype.SF_substr = String.prototype.substr;
	
	if(!String.prototype.SF_substring)
		String.prototype.SF_substring = String.prototype.substring;
	
	if(!window.SF_open && window.open)
		window.SF_open = window.open;
		
	//if(!document.SF_write && document.write)
	//	document.SF_write = document.write;

	//actuall in fact,there is need to modify the same functions in the page opened by current page.
	//property of html tag can on be lower case.
	var SF_TAG_FLAG = "sf_script";
	//document.write with replace_document will keep the change
	var flashCacheFuns=["getElementsByName"];
	for(var i=flashCacheFuns.length;i--;)
	{
		(function()
		{
			//save origin functions
			var newFunName= "SF_"+flashCacheFuns[i];
			var oldFunName = flashCacheFuns[i];
			if(!document[newFunName])
				document[newFunName] = document[oldFunName];
			//modify origin functions
			document[oldFunName]=function()
			{
				SF_FUNC_cache_flush();
				var eles=null;
				if(arguments.length==0)
					eles = document[newFunName]();
				else if(arguments.length==1)
					eles = document[newFunName](arguments[0]);
				else if(arguments.length==2)
					eles = document[newFunName](arguments[0],arguments[1]);
				else if(arguments.length==3)
					eles = document[newFunName](arguments[0],arguments[1],arguments[2]);
				else
					eles = SF_FUNC_Eval_Function(document,newFunName,Array.prototype.slice.call(arguments));
				if(oldFunName != "getElementsByTagName")
				{
					return eles;
				}
				else
				{
					var retEles = new Array();
					var retLen = 0;
					if(eles != null && typeof eles.length != "undefined")
					{
						for(var j=eles.length; j--;)//if the script is our script, don't include
						{
							if(eles[j].getAttribute(SF_TAG_FLAG) == "1")
								continue;
							retEles = [eles[j]].concat(retEles);
						}
						retEles.item = function(index)
						{
							if(index < this.length)
								return this[index];
							return null;
						};
						retEles.Nameditem = function(name)
						{
							for(var k=0;k<this.length;k++)
								if(this[k].name==name || this[k].id== name)
									return this[k];
							return null;
						};
						return retEles;
					}
					else
					{
						return eles;
					}
				}
			}
		})();
	}
	
	if(!SF_Util.isIE && document.evaluate)
	{
		if(!document["SF_evaluate"])
			document["SF_evaluate"] = document.evaluate;
		document.evaluate=function()
		{
			var argArr = Array.prototype.slice.call(arguments);
			if(argArr.length>0 && typeof(argArr[0])=="string")
			{
				if(argArr[0].match(/\[.*\]$/))
					argArr[0].replace(/\]$/,'and not(@'+ SF_TAG_FLAG +')]');
				else
					argArr[0] += '[not(@'+ SF_TAG_FLAG +')]';
			}
			switch(argArr.length)
			{
				case 0:return document["SF_evaluate"]();
				case 1:return document["SF_evaluate"](argArr[0]);
				case 2:return document["SF_evaluate"](argArr[0],argArr[1]);
				case 3:return document["SF_evaluate"](argArr[0],argArr[1],argArr[2]);
				case 4:return document["SF_evaluate"](argArr[0],argArr[1],argArr[2],argArr[3]);
				case 5:return document["SF_evaluate"](argArr[0],argArr[1],argArr[2],argArr[3],argArr[4]);
				case 6:return document["SF_evaluate"](argArr[0],argArr[1],argArr[2],argArr[3],argArr[4],argArr[6]);
				default:return SF_FUNC_Eval_Function(document,"SF_evaluate",argArr);
			}
		}
	}
	
	//querySelectorAll in IE8 doesn't support key word not.
	//need to support it in different later.
}

function SF_FUNC_GetLocation_Script_Compatible()
{
	var winlocation = window.document.__sf_location__ ;
	if(!winlocation)
	{
		winlocation = window.location["href"];
	}
	
	if(/^(javascript:|about:)/.test(winlocation))
	{
		try{
			var frame_obj = opener || parent || top;
			if(frame_obj)
				return frame_obj.window.location.href;
		}catch(e){}
	}
	return winlocation;
}
	
/** 
    *Custom the function process value of spicified url.
    *param:none
    *retvalue:no retvalue
**/
function SF_FUNC_Custom_Function()
{
	SF_FUNC_RoutineTrace("SF_FUNC_Custom_Function",arguments);
	var curURL = SF_FUNC_revertmyurl(SF_FUNC_GetLocation_Script_Compatible(),null,true);
	if(curURL && typeof(curURL) == "string")
		curURL = curURL.replace(/\/$/,'');
	
	var hostname;
	try{
		hostname = window.location.hostname;
	}catch(e){};
	
	var SF_FUNC_INDEX_STRING_INDEXOF = 0;
	var SF_FUNC_INDEX_STRING_SLICE = SF_FUNC_INDEX_STRING_INDEXOF + 1;
	var SF_FUNC_INDEX_STRING_MATCH = SF_FUNC_INDEX_STRING_SLICE + 1;
	//lwq:functions in svpn_websvc_functions.js MUST use the origin functions.
	var SF_CUSTOM_FUNCS=["indexOf","slice","match"];
	var SF_CUSTOM_PREFIX = "SF_FUNC_custom_";
	var SF_CUSTOM_INFO =[
	{
		URL:'http://news.sina.com.cn',
		FunIndex:SF_FUNC_INDEX_STRING_INDEXOF,
		Args:['https'],
		Result:-1
	},
	{
		URL:'http://dl.pconline.com.cn/download/51224.html',
		FunIndex:SF_FUNC_INDEX_STRING_MATCH,
		Obj:hostname,
		Result:{}
	}
	];
	
	
	
	for(var k=0;k<SF_CUSTOM_INFO.length;)
	{
		var cusInfoItem = SF_CUSTOM_INFO[k];
		if(!((typeof(cusInfoItem["URL"])=='string' && cusInfoItem["URL"]==curURL) || 
			(cusInfoItem["URL"].constructor==RegExp && cusInfoItem["URL"].test(curURL)))
			)
		{
			SF_CUSTOM_INFO[k] = SF_CUSTOM_INFO[SF_CUSTOM_INFO.length-1];
			SF_CUSTOM_INFO.length--;
			cusInfoItem = null;
			continue;
		}
		else
			k++;
		
		var funcIndex = cusInfoItem["FunIndex"];
		var funName = SF_CUSTOM_FUNCS[funcIndex];
		
		if(String.prototype[SF_CUSTOM_PREFIX+funName])//Make sure the function replace once.
			continue;
		else
			String.prototype[SF_CUSTOM_PREFIX+funName] = String.prototype[funName];
		
		(function(cusInfoItem)
		{
			String.prototype[funName] =function()
			{
				var argArr = Array.prototype.slice.call(arguments);
				for(var i=0;i<SF_CUSTOM_INFO.length;i++)
				{
					//try to match the this object
					if((typeof(cusInfoItem["Obj"])=="string"?(cusInfoItem["Obj"]==this):true)
						&&cusInfoItem.FunIndex==funcIndex
						&& (!cusInfoItem.Args || cusInfoItem.Args.length==argArr.length)
					)
					{
						var pattMatch = true;
						if(cusInfoItem.Args)//try to match the args.
						{
							for(var j=0;j<cusInfoItem.Args.length;j++)
							{
								if(!((typeof(cusInfoItem.Args[j]=="string") && cusInfoItem.Args[j] ==argArr[j])
									|| ((cusInfoItem.Args[j].constructor==RegExp) && cusInfoItem.Args[j].test(argArr[j])))
								)
								{
									pattMatch = false;
									break;
								}
							}
						}
						
						if(pattMatch)
							return cusInfoItem["Result"];
					}
				}
				
				return String.prototype[SF_CUSTOM_PREFIX+funName].apply(this,argArr);
			}
		})(cusInfoItem);
	}
	
}

/** 
    *Deal With the JS Function
    *param args...
     *retvalue:rewrited function
**/
function SF_FUNC_SangforFunction(/*args...*/)
{
	SF_FUNC_RoutineTrace("SF_FUNC_SangforFunction",arguments);
	if(arguments.length == 0)
		return function(){};
	var argArr = Array.prototype.slice.call(arguments);
	var maxIndex = argArr.length-1;
	var scriptBody = argArr[maxIndex];
	argArr[maxIndex] = SF_FUNC_Svr_Rewrite(scriptBody,SF_REWRITE_TYPE_JS);
	if(argArr.length == 1)
		return (new Function(argArr[0]));
	else if(argArr.length == 2)
		return (new Function(argArr[0],argArr[1]));
	else if(argArr.length == 3)
		return (new Function(argArr[0],argArr[1],argArr[2]));
	else if(argArr.length == 4)
		return (new Function(argArr[0],argArr[1],argArr[2],argArr[3]));
	else
	{
		return SF_FUNC_ExecFun("new Function",argArr);
	}
}
	
/** 
    *Deal With BASE Node
	*Desc:this function will be called after the base node by the websvr.
    *param[none]
    *retvalue:none
**/
function SF_FUNC_MARK_BASE()
{
	SF_FUNC_RoutineTrace("SF_FUNC_MARK_BASE",arguments);
	if(document.getElementsByTagName("base").length > 0)
		SF_REF_URL = document.getElementsByTagName("base")[0].href;
	else
		SF_REF_URL = SF_FUNC_GetLocation_Script_Compatible();
	window.document.SF_g_conofcurloc=SF_func_parsewinlocation(SF_REF_URL);
	SF_REF_URL = SF_FUNC_revertmyurl(SF_REF_URL,null,true);
}

/** 
    *Function for display info while debuging...
	*Desc:this function will be called after the base node by the websvr.
    *dbgInfo[exception|string]
    *retvalue:none
**/
var SF_g_WebLog = null;
SF_ENABLE_DEBUG = false;
function SF_FUNC_Debug(dbgInfo)
{
	if(!SF_ENABLE_DEBUG)
		return dbgInfo;
	
	//if(typeof(dbgInfo)=="string")
	//{
	//	var funName = SF_FUNC_Debug.caller.toString().replace(/.*function\s+(\w+)\s*\((.|\s)*/,"$1");
	//}
	
	if(!SF_g_WebLog)
	{
		if(typeof(ActiveXObject) != "undefined")
		{
			try{
				if(!SF_g_WebLog)//write log to debugview.
					SF_g_WebLog = new ActiveXObject("WebLog.WebSvcLog.1");
			}catch(e){}
		}
		else if(window.console) //write to console
		{
			SF_g_WebLog = window.console;
		}
	}
	
	if(SF_g_WebLog)
		SF_g_WebLog.log(dbgInfo); 
		
	return dbgInfo;
}

/** 
    *Used to trace routine process,placed in key function's entry.
	*funName[string]:funname
    *args[arguments]:arguments
    *retvalue:none
**/
function SF_FUNC_RoutineTrace(funName,args)
{
	if(!SF_ENABLE_DEBUG)
		return;
	var output = funName + '(';
	for(var i=0;i<args.length;i++)
	{
		
		argValue = args[i];
		if(typeof(argValue) =="object"
			|| typeof(argValue) =="function"
			|| typeof(argValue) =="undefined"
		)
			argValue = "[" + typeof(argValue) + "]";
		output += argValue + (i==(args.length-1) ? '' : ',') ;
	}
	output += ')';
	SF_FUNC_Debug(output);
}

/** 
    *A fun for SF_FUNC_CALLFUNC to check if it is a call of element.[GS]etAttribute
	*obj[object]:object call the [GS]etAttribute.
	*args[array]:arguments of the [GS]etAttribute.
    *retvalue:true if it's a call of element.[GS]etAttribute,otherwise return false;
**/
function SF_FUNC_IsEleAttributte(obj,args)
{
	SF_FUNC_RoutineTrace("SF_FUNC_IsEleAttributte",arguments);
	if (SF_func_getobjtype(obj) != SF_TYPE_ELEMENT)
		return false;
	
	if(args[0]=="innerHTML" || args[0].match(/^on\w+$/i))
		return true;
	
	for(var i=0;i<SF_const_scriptkeys.length;i++)
	{
		if(SF_const_scriptkeys[i][INDEX_ATT_NAME] == args[0].toLowerCase()
		&& SF_const_scriptkeys[i][INDEX_CALL_FUNCNAME] == 'url'
		)
		{
			return true;
		}
	}
	
	if(obj.tagName.toLowerCase() =='param'
		&& args[0].toLowerCase() == 'value'
		&& obj.getAttribute("name") == 'movie'
	)
		return true;
	return false;
}

/** 
    *construct a custom copy of location obj.
	*OBJ[window|document]:window or document object of the location.
	*Cache[bool option]:if the custom exists,with false spicified will reconstruct the obj otherwith get the cache obj.
	*retvalue:copy of location with url restored;
**/
function SF_FUNC_GetCustomLocation(OBJ,Cache)
{
	SF_FUNC_RoutineTrace("SF_FUNC_GetCustomLocation",arguments);
	try{
		if(OBJ.document){;}//test if obj.document can be read.
	}
	catch(e){return OBJ.location};
	if(!Cache || SF_FUNC_IsUndefined(OBJ["SF_LOCATION"]))
	{
		if(SF_FUNC_IsUndefined(OBJ["SF_LOCATION"]))
		{		
			if(SF_func_getobjtype(OBJ) == SF_TYPE_WINDOW 
				&& OBJ.document && !SF_FUNC_IsUndefined(OBJ.document["SF_LOCATION"]))
			{
				return OBJ["SF_LOCATION"] = OBJ.document["SF_LOCATION"];
			}
			
			OBJ["SF_LOCATION"] = {};
		}
		
		if(SF_func_getobjtype(OBJ) == SF_TYPE_WINDOW && OBJ.document)//make sure document and window use the same cus location obj;
			OBJ.document["SF_LOCATION"] = OBJ["SF_LOCATION"];
		
		var cusLocation = OBJ["SF_LOCATION"];
		if(OBJ.location.href=="about:blank")
		{
			cusLocation["hash"]="";
			cusLocation["search"]="";
			cusLocation["protocol"]="about:";
			cusLocation["host"]="";
			cusLocation["port"]="";
			cusLocation["pathname"]="blank"
			cusLocation["href"]="about:blank";
			cusLocation["hostname"]="";
		}
		else
		{
			try
			{
				var curLocationObj = SF_func_parsewinlocation(OBJ.location.href);
				for(var key in curLocationObj["location"])
				{
					cusLocation[key] = curLocationObj["location"][key];
				}
			}catch(e)
			{
				if(/^javascript:/.test(OBJ.location.href))
				{
					cusLocation["hash"]="";
					cusLocation["search"]="";
					cusLocation["protocol"]="";
					cusLocation["host"]="";
					cusLocation["port"]="";
					cusLocation["pathname"]=""
					cusLocation["href"]=""
					cusLocation["hostname"]="";
				}
			}
		}
		cusLocation["toString"]=function(){return this.href;};
		cusLocation["assign"]=function(){
			arguments[0] = SF_FUNC_transhttpurl(arguments[0],1,window.document.SF_g_conofcurloc);
			if(arguments.length>0)OBJ.location["assign"](arguments[0]);
			SF_FUNC_GetCustomLocation(OBJ,false);
		};
		
		cusLocation["replace"]=function(){
			arguments[0] = SF_FUNC_transhttpurl(arguments[0],1,window.document.SF_g_conofcurloc);
			if(arguments.length>0)
				OBJ.location["replace"](arguments[0]);
			SF_FUNC_GetCustomLocation(OBJ,false);
		};
		
		
		
		cusLocation["reload"]=function(){
			arguments[0] = SF_FUNC_transhttpurl(arguments[0],1,window.document.SF_g_conofcurloc);
			if(arguments.length>0)
				OBJ.location["reload"](arguments[0]);
			else
				OBJ.location["reload"]();
				
			SF_FUNC_GetCustomLocation(OBJ,false);
		};
		
		cusLocation["SF_WINDOW"] = OBJ;
	}
	
	//Sync Custom Functions
	var selfFuns = {"toString":1,"assign":1,"replace":1,"reload":1};
	for(var key in OBJ.location)
	{
		if(typeof(OBJ.location[key])=="function" && !(key in selfFuns))
			OBJ["SF_LOCATION"][key] = OBJ.location[key];
	}
		
	return OBJ["SF_LOCATION"];
}

/** 
    *process tag attribute with like vbscript:a.keyword.reload will be rewrited as a.getkeywrod().reload as a getatt stmt,not a fun call.
	*input[anytype]:result of attribute code evaluted result
	*retvalue:evalute result of the expression.
**/
function SF_FUNC_VBHref(input)
{
	SF_FUNC_RoutineTrace("SF_FUNC_VBHref",arguments);
	if(typeof(input)=="function")
		return input();
	else
		return input;
}

/** 
    *Get the cookie flag used for pluging open url with twfid;
	*input[string]:src string;
	*retvalue:string with cookie flag inserted to the tail.
**/
function SF_FUNC_InsertCookieFlag(input)
{
	SF_FUNC_RoutineTrace("SF_FUNC_InsertCookieFlag",arguments);
	var SF_COOKIE_FLAG = "sftwf%TWFID%/";
	var twfid = null;
	var cookies = document.cookie.split("; ")
	for (var i=0; i < cookies.length; i++)
	{
		var aCrumb = cookies[i].split("=");
		if ("twfid" == aCrumb[0].toLowerCase())
		{
			twfid = unescape(aCrumb[1]);
			break;
		}
	}
	
	var cookieFlag = SF_COOKIE_FLAG.replace('%TWFID%',twfid);
	if(twfid)
	{
		if(/safeurl\/web\/\d\//i.test(input))
		{
			return input.replace(/(safeurl\/web\/\d\/)/,function (){return cookieFlag + arguments[1];});
		}
		else if(/web\/\d\//i.test(input))
		{
			return input.replace(/(web\/\d\/)/,function (){return cookieFlag + arguments[1];})
		}
	}
	
	return input;
}

/** 
    *Check and remove the Cookie falg from input string;
	*input[string]:src string;
	*retvalue:str without Cookie flag;
**/
function SF_FUNC_RemoveCookieFlag(input)
{
	SF_FUNC_RoutineTrace("SF_FUNC_RemoveCookieFlag",arguments);
	var SF_COOKIE_REG = /\/sftwf[a-f0-9]+\//gi;
	return input.replace(SF_COOKIE_REG,'/');
}

function SF_FUNC_md5(str) 
{
	var xl;
	var rotateLeft = function (lValue, iShiftBits) {
		return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
	};

	var addUnsigned = function (lX,lY) {
		var lX4,lY4,lX8,lY8,lResult;
		lX8 = (lX & 0x80000000);
		lY8 = (lY & 0x80000000);
		lX4 = (lX & 0x40000000);
		lY4 = (lY & 0x40000000);
		lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
		if (lX4 & lY4) {
			return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
		}
		if (lX4 | lY4) {
			if (lResult & 0x40000000) {
				return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
			} else {
				return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
			}
		} else {
			return (lResult ^ lX8 ^ lY8);
		}
	};

	var _F = function (x,y,z) { return (x & y) | ((~x) & z); };
	var _G = function (x,y,z) { return (x & z) | (y & (~z)); };
	var _H = function (x,y,z) { return (x ^ y ^ z); };
	var _I = function (x,y,z) { return (y ^ (x | (~z))); };

	var _FF = function (a,b,c,d,x,s,ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(_F(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};

	var _GG = function (a,b,c,d,x,s,ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(_G(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};

	var _HH = function (a,b,c,d,x,s,ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(_H(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};

	var _II = function (a,b,c,d,x,s,ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(_I(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};

	var convertToWordArray = function (str) {
		var lWordCount;
		var lMessageLength = str.length;
		var lNumberOfWords_temp1=lMessageLength + 8;
		var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
		var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
		var lWordArray=new Array(lNumberOfWords-1);
		var lBytePosition = 0;
		var lByteCount = 0;
		while ( lByteCount < lMessageLength ) {
			lWordCount = (lByteCount-(lByteCount % 4))/4;
			lBytePosition = (lByteCount % 4)*8;
			lWordArray[lWordCount] = (lWordArray[lWordCount] | (str.charCodeAt(lByteCount)<<lBytePosition));
			lByteCount++;
		}
		lWordCount = (lByteCount-(lByteCount % 4))/4;
		lBytePosition = (lByteCount % 4)*8;
		lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
		lWordArray[lNumberOfWords-2] = lMessageLength<<3;
		lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
		return lWordArray;
	};

	var wordToHex = function (lValue) {
		var wordToHexValue="",wordToHexValue_temp="",lByte,lCount;
		for (lCount = 0;lCount<=3;lCount++) {
			lByte = (lValue>>>(lCount*8)) & 255;
			wordToHexValue_temp = "0" + lByte.toString(16);
			wordToHexValue = wordToHexValue + wordToHexValue_temp.substr(wordToHexValue_temp.length-2,2);
		}
		return wordToHexValue;
	};

	var x=[],
		k,AA,BB,CC,DD,a,b,c,d,
		S11=7, S12=12, S13=17, S14=22,
		S21=5, S22=9 , S23=14, S24=20,
		S31=4, S32=11, S33=16, S34=23,
		S41=6, S42=10, S43=15, S44=21;

	str = SF_FUNC_utf8_encode(str);
	x = convertToWordArray(str);
	a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
	
	xl = x.length;
	for (k=0;k<xl;k+=16) {
		AA=a; BB=b; CC=c; DD=d;
		a=_FF(a,b,c,d,x[k+0], S11,0xD76AA478);
		d=_FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
		c=_FF(c,d,a,b,x[k+2], S13,0x242070DB);
		b=_FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
		a=_FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
		d=_FF(d,a,b,c,x[k+5], S12,0x4787C62A);
		c=_FF(c,d,a,b,x[k+6], S13,0xA8304613);
		b=_FF(b,c,d,a,x[k+7], S14,0xFD469501);
		a=_FF(a,b,c,d,x[k+8], S11,0x698098D8);
		d=_FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
		c=_FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
		b=_FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
		a=_FF(a,b,c,d,x[k+12],S11,0x6B901122);
		d=_FF(d,a,b,c,x[k+13],S12,0xFD987193);
		c=_FF(c,d,a,b,x[k+14],S13,0xA679438E);
		b=_FF(b,c,d,a,x[k+15],S14,0x49B40821);
		a=_GG(a,b,c,d,x[k+1], S21,0xF61E2562);
		d=_GG(d,a,b,c,x[k+6], S22,0xC040B340);
		c=_GG(c,d,a,b,x[k+11],S23,0x265E5A51);
		b=_GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
		a=_GG(a,b,c,d,x[k+5], S21,0xD62F105D);
		d=_GG(d,a,b,c,x[k+10],S22,0x2441453);
		c=_GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
		b=_GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
		a=_GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
		d=_GG(d,a,b,c,x[k+14],S22,0xC33707D6);
		c=_GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
		b=_GG(b,c,d,a,x[k+8], S24,0x455A14ED);
		a=_GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
		d=_GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
		c=_GG(c,d,a,b,x[k+7], S23,0x676F02D9);
		b=_GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
		a=_HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
		d=_HH(d,a,b,c,x[k+8], S32,0x8771F681);
		c=_HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
		b=_HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
		a=_HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
		d=_HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
		c=_HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
		b=_HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
		a=_HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
		d=_HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
		c=_HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
		b=_HH(b,c,d,a,x[k+6], S34,0x4881D05);
		a=_HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
		d=_HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
		c=_HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
		b=_HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
		a=_II(a,b,c,d,x[k+0], S41,0xF4292244);
		d=_II(d,a,b,c,x[k+7], S42,0x432AFF97);
		c=_II(c,d,a,b,x[k+14],S43,0xAB9423A7);
		b=_II(b,c,d,a,x[k+5], S44,0xFC93A039);
		a=_II(a,b,c,d,x[k+12],S41,0x655B59C3);
		d=_II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
		c=_II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
		b=_II(b,c,d,a,x[k+1], S44,0x85845DD1);
		a=_II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
		d=_II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
		c=_II(c,d,a,b,x[k+6], S43,0xA3014314);
		b=_II(b,c,d,a,x[k+13],S44,0x4E0811A1);
		a=_II(a,b,c,d,x[k+4], S41,0xF7537E82);
		d=_II(d,a,b,c,x[k+11],S42,0xBD3AF235);
		c=_II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
		b=_II(b,c,d,a,x[k+9], S44,0xEB86D391);
		a=addUnsigned(a,AA);
		b=addUnsigned(b,BB);
		c=addUnsigned(c,CC);
		d=addUnsigned(d,DD);
	}

	var temp = wordToHex(a)+wordToHex(b)+wordToHex(c)+wordToHex(d);

	return temp.toLowerCase();
}

function SF_FUNC_utf8_encode ( argString ) 
{
	var string = (argString+''); // .replace(/\r\n/g, "\n").replace(/\r/g, "\n");

	var utftext = "";
	var start, end;
	var stringl = 0;

	start = end = 0;
	stringl = string.length;
	for (var n = 0; n < stringl; n++) {
		var c1 = string.charCodeAt(n);
		var enc = null;

		if (c1 < 128) {
			end++;
		} else if (c1 > 127 && c1 < 2048) {
			enc = String.fromCharCode((c1 >> 6) | 192) + String.fromCharCode((c1 & 63) | 128);
		} else {
			enc = String.fromCharCode((c1 >> 12) | 224) + String.fromCharCode(((c1 >> 6) & 63) | 128) + String.fromCharCode((c1 & 63) | 128);
		}
		if (enc !== null) {
			if (end > start) {
				utftext += string.substring(start, end);
			}
			utftext += enc;
			start = end = n+1;
		}
	}

	if (end > start) {
		utftext += string.substring(start, string.length);
	}

	return utftext;
}

/** 
    *DIGEST a content;
	*content[string]:cache content before rewrite;
	*retvalue[bool]:digest hash.
**/
function SF_FUNC_DIGEST(content)
{
	var HASH_MIN_SIZE = 512;
	var HASH_SEGMENT_SIZE = 512;
	var HASH_DIGEST_SIZE = 128;
	if(content.length <= HASH_MIN_SIZE )
		return SF_Encrypt.crc32(content);
	
	var segNum = Math.round(content.length / HASH_SEGMENT_SIZE);
	//like 1025/1024
	if(content.length % HASH_SEGMENT_SIZE < (HASH_SEGMENT_SIZE/2))
		++segNum;
	
	var digestTxt = "";
	for(var i=0;i<segNum;i++)
	{
		digestTxt += i*(HASH_SEGMENT_SIZE)+ "|" + content.substr(i*(HASH_SEGMENT_SIZE),HASH_DIGEST_SIZE) + "|" ;
	}
	
	return SF_FUNC_md5(digestTxt);
}

/** 
    *get rewrited from cache
	*content[string]:cache content before rewrite;
	*type[int]:script type,css,js,vb,html.
	*flag[int]:rewrite type,
	*retvalue[bool]:rewrited value if found,null otherwise.
**/
function SF_FUNC_GetRewriteCache(content,type,flag)
{
	
	var hashSimple = type + "|"+flag + "|" + content.length;
	
	if(	SF_g_CacheRewrite && SF_g_CacheRewrite[hashSimple] )
	{
		var hashContent = SF_FUNC_DIGEST(content);
		if(SF_g_CacheRewrite[hashSimple][hashContent])
		{
			if(typeof(SF_g_CacheRewrite[hashSimple][hashContent]["count"]) == "number")
				++SF_g_CacheRewrite[hashSimple][hashContent]["count"];
			else
				SF_g_CacheRewrite[hashSimple][hashContent]["count"] = 1;
				
			return SF_g_CacheRewrite[hashSimple][hashContent]["content"];
		}
	}
	return null;
}

/** 
    *Cache rewrite info in hash tables.
	*content[string]:cache content before rewrite;
	*type[int]:script type,css,js,vb,html.
	*flag[int]:rewrite type,
	*value[string]:cache content,
	*retvalue:no;
**/
function SF_FUNC_CacheRewrite(content,type,flag,value)
{
	if(!SF_g_CacheRewrite)
		SF_g_CacheRewrite = {};
	
	if(typeof(SF_g_CacheRewriteNum) != 'number')
		SF_g_CacheRewriteNum = 0;
		
	var hashSimple = type + "|"+flag + "|" + content.length;
	var hashContent = SF_FUNC_DIGEST(content);
	
	//use LFU to swap the cache.
	if(SF_g_CacheRewriteNum >= 20)
	{
		var minContentHash = null,minSimplyHash = null;
		var minCount = 0;
		for(var firstHash in SF_g_CacheRewrite)
		{
			for(var secondHash in SF_g_CacheRewrite[firstHash])
			{
				if(--SF_g_CacheRewrite[firstHash][secondHash].count < 0)
				{
					delete SF_g_CacheRewrite[firstHash][secondHash];
					--SF_g_CacheRewriteNum;
					continue;
				}
				
				if(SF_g_CacheRewrite[firstHash][secondHash].count <= minCount || minCount==0)
				{
					minCount = SF_g_CacheRewrite[firstHash][secondHash].count;
					minSimplyHash = firstHash;
					minContentHash = secondHash;
				}
			}
		}
		
		//eliminate a cache item.
		if(minSimplyHash && minContentHash)
		{
			delete SF_g_CacheRewrite[minSimplyHash][minContentHash];
			--SF_g_CacheRewriteNum;
		}
	}
	
	//cache content;
	if(!SF_g_CacheRewrite[hashSimple])
		SF_g_CacheRewrite[hashSimple] = {};
	
	if(!SF_g_CacheRewrite[hashSimple][hashContent])
		SF_g_CacheRewrite[hashSimple][hashContent] = {};
		
	//increase the count
	SF_g_CacheRewrite[hashSimple][hashContent].content = value;
	SF_g_CacheRewrite[hashSimple][hashContent].count = 0;
	++SF_g_CacheRewriteNum;
}

/** 
    *API,used with SF_FUNC_XXX_DEF func,to check the object and be used as refer in SF_FUNC_XXX_DEF parameter. 
	*OBJ[any type]:target variable.
	*retvalue[bool]:return true if obj is refer object,otherwise return false;
**/
function SF_FUNC_IsReferObj(OBJ)
{
    return (typeof(OBJ) == 'object' || typeof(OBJ) == 'function') && !!OBJ;
}

/** 
    *Utiti Function,get element's outerHTML.
	*OBJ[element]:target object.
	*retvalue[string]:outerHTML of the element;
**/
function SF_FUNC_GetOuterHTML(ele)
{
    if (!ele) 
		return null;
	
	if(!SF_FUNC_IsUndefined(ele["outerHTML"]))
		return ele.outerHTML;
	
	var element = document.createElement("div");
	element.appendChild(ele.cloneNode(true));
	return element.innerHTML;
}


function SF_FUNC_SyncRequest(reqObj)
{
	SF_FUNC_RoutineTrace("SF_FUNC_Request",arguments);
	var xmlhttp = null;
	xmlhttp = SF_func_createxmlhttp();
	if(xmlhttp == null)
		return "";
	
	xmlhttp.open(reqObj['method'], reqObj['url'] , false);	
	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	
	xmlhttp.setRequestHeader('X-SVPN-REFURL', SF_FUNC_GET_location().href);
	xmlhttp.setRequestHeader('X-SF-GET-ORIG', "yes");
	
	try{
		xmlhttp.send(reqObj['body']);
	}
	catch (e){
		SF_FUNC_Debug(e.message);
		return "";
	}

	return SF_func_trim(xmlhttp.responseText ? xmlhttp.responseText : "");
}


/** 
    *Part of custom rewrite,used to get origin page of current page.
	*url[string,option]:url to request,if url is null or missed,locatin.href is used.
	*retvalue[string]:Origin content of the url,even the url is customly redirected on the server side.
**/
function SF_FUNC_GetOrigContent(url,method,postdata)
{
	if(!url)
		url = SF_FUNC_GetLocation_Script_Compatible();
	
	if(!method)
		method = "GET";
		
	if(!postdata)	
		postdata = null;
		
	url = url.replace(/#.*$/,'');
	if(url.indexOf('?')!=-1)
		url += "&sfrnd="+Math.random();
	else
		url += "?sfrnd="+Math.random();
	
	var reqObj = {
		"url":url,
		"method":method,
		"body":postdata
	};
	
	return SF_FUNC_SyncRequest(reqObj);
}

function SF_SinforCreateObject(name){
	return new ActiveXObject(name);
}

SF_FUNC_WEB_SERVER_INIT();