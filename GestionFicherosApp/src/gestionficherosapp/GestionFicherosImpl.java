package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros{
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas =0;
	private int columnas =3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;
	
	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {
		// TODO Auto-generated method stub
		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {
		// TODO Auto-generated method stub
		
		if (carpetaDeTrabajo.getParentFile()!= null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}
		
		
	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File nuevo = new File(carpetaDeTrabajo,arg0);
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Error. Se esperaba un directorio, pero "+ 
					nuevo.getAbsolutePath() + " No EXISTE");
		}else if(!carpetaDeTrabajo.exists()) {
			throw new GestionFicherosException("no existe la carpeta padre");
		//Accion
		}else {
			nuevo.mkdir();
			actualiza();
		}
		
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file= new File(carpetaDeTrabajo,arg0);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!file.canWrite()) {
			throw new GestionFicherosException ("Error. la ruta "+ 
		file.getAbsoluteFile() + " No lo es");
		}
		if (!file.isDirectory()) {
			throw new GestionFicherosException ("Error. Se esperaba un directorio, pero "+ 
		file.getAbsolutePath() + " No EXISTE");
		}
		if (!file.canRead()) {
			throw new GestionFicherosException ("No tienes permiso de lectura ");
		}
		actualiza();
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File borrar = new File(carpetaDeTrabajo,arg0);
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("No tienes permiso de lectura");
		}else if(!carpetaDeTrabajo.exists()) {
			throw new GestionFicherosException("Error. Se esperaba un directorio, pero "+ 
					borrar.getAbsolutePath() + " No EXISTE");
		}else if(!borrar.exists()) {
			throw new GestionFicherosException("no existe el archivo");
		}else {
			borrar.delete();
			actualiza();
		}	
		
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
		File file = new File(carpetaDeTrabajo,arg0);
		
		if (!file.exists()) {
			throw new GestionFicherosException ("Error. la ruta "+ 
		file.getAbsolutePath() + " No lo es");
		}
		if (!file.isDirectory()) {
			throw new GestionFicherosException ("Error. Se esperaba un directorio, pero "+ 
		file.getAbsolutePath() + " No EXISTE");
		}
		if (!file.canRead()) {
			throw new GestionFicherosException ("No tienes permiso de lectura ");
		}
		carpetaDeTrabajo = file;
		actualiza();	
	}

	@Override
	public int getColumnas() {
		// TODO Auto-generated method stub
		return this.columnas;
	}

	@Override
	public Object[][] getContenido() {
		// TODO Auto-generated method stub
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		// TODO Auto-generated method stub
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		// TODO Auto-generated method stub
		return this.filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		// TODO Auto-generated method stub
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File (carpetaDeTrabajo,arg0);
		Date d = new Date(file.lastModified());
		String cadena = null;
		StringBuilder sb = new StringBuilder();
		cadena ="---------------- Informacion de fichero------------";
		sb.append("Nombre:"+file.getName()+"\n");
		sb.append("Directorio:"+file.getParent()+"\n");
		if (file.isFile()) {
			sb.append("Tipo: Fichero \n");
			sb.append("Informacion unica de Ficheros:\n");
			sb.append("  |Tamaño: "+(file.getTotalSpace()/8)+" bytes\n");
		}else {
			sb.append("Tipo: Directorio \n");
			sb.append("Informacion unica de Directorios:\n");
			sb.append("  |Numero de Elementos: "+file.list().length+"\n");
			sb.append("  |Espacio libre: "+(file.getFreeSpace()/1000000000)+"GB\n");
			sb.append("  |Espacio disponible: "+(file.getUsableSpace()/1000000000)+"GB\n");
			sb.append("  |Espacio total: "+(file.getTotalSpace()/1000000000)+"GB\n");
		}
		sb.append("Ultima vez modificado: "+d.toString()+"\n");								//Se usa el Date creado anteriormente para imprimir la ultima modificacion
		sb.append("Esta oculto? "+(file.isHidden() ? "Si" : "No")+"\n");	
		return sb.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		// TODO Auto-generated method stub
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		// TODO Auto-generated method stub
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
		File file1 = new File(carpetaDeTrabajo,arg1);
		file.renameTo(file1);
		actualiza();
		if (file.renameTo(file1)) {
			throw new GestionFicherosException ("El nombre de archivo ya existe");		
		}
		
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		// TODO Auto-generated method stub
		this.columnas = arg0;
	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(arg0);
		
		if (!file.exists()) {
			throw new GestionFicherosException ("Error. la ruta "+ 
		file.getAbsolutePath() + " No lo es");
		}
		if (!file.isDirectory()) {
			throw new GestionFicherosException ("Error. Se esperaba un directorio, pero "+ 
		file.getAbsolutePath() + " No EXISTE");
		}
		if (!file.canRead()) {
			throw new GestionFicherosException ("No tienes permiso de lectura ");
		}
		carpetaDeTrabajo = file;
		actualiza();
	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}
	
}
