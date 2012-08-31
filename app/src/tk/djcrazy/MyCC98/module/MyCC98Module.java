package tk.djcrazy.MyCC98.module;

import tk.djcrazy.libCC98.CC98ClientImpl;
import tk.djcrazy.libCC98.CC98ParserImpl;
import tk.djcrazy.libCC98.CC98ServiceImpl;
import tk.djcrazy.libCC98.CC98UrlManagerImpl;
import tk.djcrazy.libCC98.ICC98Client;
import tk.djcrazy.libCC98.ICC98Parser;
import tk.djcrazy.libCC98.ICC98Service;
import tk.djcrazy.libCC98.ICC98UrlManager;

import com.google.inject.AbstractModule;

public class MyCC98Module extends AbstractModule{

	@Override
	protected void configure() {
		bind(ICC98Client.class).to(CC98ClientImpl.class);
		bind(ICC98Parser.class).to(CC98ParserImpl.class);
		bind(ICC98Service.class).to(CC98ServiceImpl.class);
		bind(ICC98UrlManager.class).to(CC98UrlManagerImpl.class);
	}
}
