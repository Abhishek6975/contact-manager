/** @type {import('tailwindcss').Config} */
export default {
  content: [
	'./src/main/resources/templates/**/*.html',
    './src/main/resources/static/**/*.js',
	
	/*'src/main/resources**.{html,js}'*/
	],
  theme: {
    extend: {},
  },
  plugins: [],
  darkMode:"selector"
}

