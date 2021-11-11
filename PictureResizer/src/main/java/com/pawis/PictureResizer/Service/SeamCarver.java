package com.pawis.PictureResizer.Service;


public class SeamCarver {
	// private final Picture pic;
	private double[][] energy;
	private int[][] colors;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new IllegalArgumentException();
		}
		Picture pic = new Picture(picture);
		this.colors = new int[pic.height()][pic.width()];
		this.energy = new double[colors.length][colors[0].length];
		for (int col = 0; col < pic.height(); col++) {
			for (int row = 0; row < pic.width(); row++) {
				colors[col][row] = pic.getRGB(row, col);
			}
		}
	}

	// current picture
	public Picture picture() {
		// a
		Picture newPic = new Picture(colors[0].length, colors.length);
		for (int col = 0; col < colors.length; col++) {
			for (int row = 0; row < colors[col].length; row++) {
				newPic.setRGB(row, col, colors[col][row]);
			}
		}
		return newPic;
	}

	// width of current picture
	public int width() {
		return colors[0].length;
	}

	// height of current picture
	public int height() {
		return colors.length;
	}

	private int xEnergy(int x, int y) {
		if (check(x, y)) {
			throw new IllegalArgumentException();
		}

		int xR = Math.abs(((colors[y][x + 1] >> 16) & 0xFF) - ((colors[y][x - 1] >> 16) & 0xFF));
		int xG = Math.abs(((colors[y][x + 1] >> 8) & 0xFF) - ((colors[y][x - 1] >> 8) & 0xFF));
		int xB = Math.abs(((colors[y][x + 1] & 0xFF) - (colors[y][x - 1] & 0xFF)));
		int xEnergy = (int) Math.pow(xR, 2) + (int) Math.pow(xG, 2) + (int) Math.pow(xB, 2);

		return xEnergy;
	}

	private int yEnergy(int x, int y) {
		if (check(x, y)) {
			throw new IllegalArgumentException();
		}

		int yR = Math.abs(((colors[y + 1][x] >> 16) & 0xFF) - ((colors[y - 1][x] >> 16) & 0xFF));
		int yG = Math.abs(((colors[y + 1][x] >> 8) & 0xFF) - ((colors[y - 1][x] >> 8) & 0xFF));
		int yB = Math.abs(((colors[y + 1][x] & 0xFF) - (colors[y - 1][x] & 0xFF)));
		int yEnergy = (int) Math.pow(yR, 2) + (int) Math.pow(yG, 2) + (int) Math.pow(yB, 2);

		return yEnergy;
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if (check(x, y)) {
			throw new IllegalArgumentException();
		}
		if (x == 0 || y == 0 || y == colors.length - 1 || x == colors[0].length - 1) {
			return 1000;
		} else {
			return Math.sqrt(xEnergy(x, y) + yEnergy(x, y));
		}
	}

	private boolean check(int x, int y) {
		boolean check = false;
		if (x >= width() || y >= height() || x < 0 || y < 0) {
			check = true;
		}
		return check;
	}

	private double[][] transpose(double[][] array) {
		if (array == null || array.length == 0)
			return array;

		int width = array.length;
		int height = array[0].length;

		double[][] arraynew = new double[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				arraynew[y][x] = array[x][y];
			}
		}
		return arraynew;
	}

	private int[][] transposeInt(int[][] array) {
		if (array == null || array.length == 0)
			return array;

		int width = array.length;
		int height = array[0].length;

		int[][] arraynew = new int[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				arraynew[y][x] = array[x][y];
			}
		}
		return arraynew;
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		energy = transpose(energy);
		colors = transposeInt(colors);
		int[] seam = findVerticalSeam();
		energy = transpose(energy);
		colors = transposeInt(colors);
		return seam;

	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		for (int col = 0; col < energy.length; col++) {
			for (int row = 0; row < energy[col].length; row++) {
				energy[col][row] = energy(row, col);

			}
		}
		double[][] energyTo = new double[energy.length][energy[0].length];
		int[][] edgeTo = new int[energy.length][energy[0].length];
		for (int col = 0; col < energyTo.length; col++) {
			for (int row = 0; row < energyTo[col].length; row++) {
				energyTo[col][row] = 0;
				if (col == 0 && row < energy[0].length) {
					energyTo[col][row] = 1000;
					edgeTo[col][row] = -1;
				}

			}
		}

		for (int col = 0; col < energy.length; col++) {
			for (int row = 0; row < energy[col].length; row++) {
				if (col + 1 < energy.length && row - 1 >= 0) {
					if (energyTo[col + 1][row - 1] == 0) {
						energyTo[col + 1][row - 1] = energyTo[col][row] + energy[col + 1][row - 1];
						edgeTo[col + 1][row - 1] = row;
					} else if (energyTo[col][row] + energy[col + 1][row - 1] < energyTo[col + 1][row - 1]) {
						energyTo[col + 1][row - 1] = energyTo[col][row] + energy[col + 1][row - 1];
						edgeTo[col + 1][row - 1] = row;
					}
				}

				if (col + 1 < energy.length) {
					if (energyTo[col + 1][row] == 0) {
						energyTo[col + 1][row] = energyTo[col][row] + energy[col + 1][row];
						edgeTo[col + 1][row] = row;
					} else if (energyTo[col][row] + energy[col + 1][row] < energyTo[col + 1][row]) {
						energyTo[col + 1][row] = energyTo[col][row] + energy[col + 1][row];
						edgeTo[col + 1][row] = row;
					}
				}

				if (col + 1 < energy.length && row + 1 < energy[0].length) {
					if (energyTo[col + 1][row + 1] == 0) {
						energyTo[col + 1][row + 1] = energyTo[col][row] + energy[col + 1][row + 1];
						edgeTo[col + 1][row + 1] = row;
					} else if (energyTo[col][row] + energy[col + 1][row + 1] < energyTo[col + 1][row + 1]) {
						energyTo[col + 1][row + 1] = energyTo[col][row] + energy[col + 1][row + 1];
						edgeTo[col + 1][row + 1] = row;
					}
				}
			}
		}

		int[] seam = new int[energy.length];
		int index = 0;
		double smallest = Double.POSITIVE_INFINITY;
		for (int i = 0; i < energy[0].length; i++) {
			if (energyTo[energy.length - 1][i] < smallest) {
				smallest = energyTo[energy.length - 1][i];
				index = i;
			}
		}
		seam[energy.length - 1] = index;
		int j = energy.length - 1;
		while (j > 0) {
			index = edgeTo[j][index];
			seam[j - 1] = index;
			j--;
		}
		return seam;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) {
			throw new IllegalArgumentException();
		}
		if (seam.length == 0)
			seam = findHorizontalSeam();
		energy = transpose(energy);
		colors = transposeInt(colors);
		removeVerticalSeam(seam);
		energy = transpose(energy);
		colors = transposeInt(colors);
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null || seam.length != colors.length) {
			throw new IllegalArgumentException();
		}
		if (width() <= 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < seam.length - 1; i++) {
			if ((seam[i] - (seam[i + 1])) > 1 || (seam[i] - (seam[i + 1])) < -1) {
				throw new IllegalArgumentException();
			}
		}
		for (int i = 0; i < seam.length; i++) {
			if (seam[i] > colors[0].length - 1 || seam[i] < 0) {
				throw new IllegalArgumentException();
			}
		}

		if (seam.length == 0)
			seam = findVerticalSeam();
		int[][] newColors = new int[colors.length][colors[0].length - 1];
		double[][] newEnergy = new double[colors.length][colors[0].length - 1];
		for (int i = 0; i < colors.length; i++) {
			System.arraycopy(colors[i], seam[i] + 1, colors[i], seam[i], colors[0].length - 1 - seam[i]);
			System.arraycopy(energy[i], seam[i] + 1, energy[i], seam[i], energy[0].length - 1 - seam[i]);
		}

		for (int i = 0; i < newColors.length; i++) {
			System.arraycopy(colors[i], 0, newColors[i], 0, newColors[i].length);
			System.arraycopy(energy[i], 0, newEnergy[i], 0, newEnergy[i].length);
		}
		colors = newColors;
		energy = newEnergy;
	}

	// unit testing (optional)
	public static void main(String[] args) {
	

	}

}
