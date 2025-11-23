import CoreMiddleware from './core/apiMiddleware';
import HomeMiddleware from '../scenes/Main/scenes/Bobb/Home/HomeMiddleware';

export default [...HomeMiddleware, ...CoreMiddleware];
