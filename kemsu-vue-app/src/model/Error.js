/*
 * 
 * 
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 *
 * Swagger Codegen version: 3.0.54
 *
 * Do not edit the class manually.
 *
 */
import { entries } from 'core-js/actual/array';
import ApiClient from '../ApiClient';
import StatusCode from './StatusCode';

/**
 * The Error model module.
 * @module model/Error
 * @version 1.0.0
 */
export default class Error {
  /**
   * Constructs a new <code>Error</code>.
   * @alias module:model/Error
   * @class
   * @param status {module:model/StatusCode} 
   */
  constructor(status) {
    this.status = status;
  }

  /**
   * Constructs a <code>Error</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/Error} obj Optional instance to populate.
   * @return {module:model/Error} The populated <code>Error</code> instance.
   */
  static constructFromObject(data, obj) {
    if (data) {
      obj = obj || new Error();
      if (data.prototype.hasOwnProperty.call(entries, 'status'))
        obj.status = StatusCode.constructFromObject(data['status']);
      if (data.prototype.hasOwnProperty.call(entries, 'message'))
        obj.message = ApiClient.convertToType(data['message'], 'String');
    }
    return obj;
  }
}

/**
 * @member {module:model/StatusCode} status
 */
Error.prototype.status = undefined;

/**
 * @member {String} message
 */
Error.prototype.message = undefined;

