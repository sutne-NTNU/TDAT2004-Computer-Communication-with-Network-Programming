import axios from 'axios';
import properties from '../properties.json';

const url = properties.serverURL;



class Service
{
	run(language)
	{
		return axios.post(url + language.url, {code: language.code}).then(response => response.data);
	}
}



export let service = new Service();