import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {deletePetRequest, fetchPets, postPet} from "../actions/PetActions";
import {GlobalState} from "../App";
import {Link} from "react-router-dom"
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import Accordion from "react-bootstrap/Accordion";
import Card from "react-bootstrap/Card";
import {Appointment} from "./AppointmentList";
import {deleteAdminRequest, fetchAdmins, postAdmin} from "../actions/AdminActions";

export interface Admin {
    id: number,
    employeeID: number;
    photo: string;
    address: string;
    password: string;
    username: string;
    name: string,
    cellphone: number,
    email: string
}


export interface AdminState {
    admins: Admin[],
    admin: Admin,
    isFetching: boolean
}


type FormData = {
    adminName: string;
    adminCellphone: number;
    adminEmail: string;
    adminPhoto: string;
    adminAddress: string;
    adminPassword: string;
    adminUsername: string;
}




const ProtoAdminList = (props: { admins: Admin[], isFetching: boolean, loadAdmins: () => void, postAdmin: (admin: Admin) => void, deleteAdmin: (id: number) => void }) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({adminName, adminCellphone, adminEmail, adminPhoto, adminAddress,adminPassword,adminUsername}) => {
        console.log(adminName, adminCellphone, adminEmail, adminPhoto, adminAddress,adminPassword,adminUsername);
        props.postAdmin({
            address: adminAddress,
            employeeID: 0,
            id: 0,
            password: adminPassword,
            photo: adminPhoto,
            username: adminUsername,
            name: adminName, cellphone: adminCellphone, email: adminEmail });
        setUpdate(true);
        setValue("adminName", "");
        setValue("adminCellphone", -1);
        setValue("adminEmail", "");
        setValue("adminPhoto", "");
        setValue("adminAddress", "");
        setValue("adminPassword", "");
        setValue("adminUsername", "");
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadAdmins();
        return () => {
            setUpdate(false)
        }
    }, [update]);

    let list = props.admins.map((admin: Admin) => {
        return (
            <ListGroup.Item key={admin.id}>
                <Link to={`/pet/${admin.id}`}>{admin.name}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deleteAdmin(admin.id);
                    setUpdate(true)
                }}>Delete</Button>
            </ListGroup.Item>

        )
    });

    let emptyList = (<p className="text-center">Currently don't have any admins registered!</p>)

    let adminList = ((props.admins.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList)


    return (
        <div>
            <br/>
            <h1 className="text-center">Admins</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : adminList}

            <Accordion>
                <Card>
                    <Card.Header>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0">
                            Add new Admin
                        </Accordion.Toggle>
                    </Card.Header>
                    <Accordion.Collapse eventKey="0">
                        <Card.Body>
                            <form onSubmit={onSubmit}>
                                <div className="form-group">
                                    <label>Name</label>
                                    <input className="form-control" id="adminName" name="adminName" ref={register({required: true})}/>
                                    {errors.adminName && 'Admin name is required'}
                                </div>
                                <div className="form-group">
                                    <label>Username</label>
                                    <input className="form-control" id="adminUsername" name="adminUsername" ref={register({required: true})}/>
                                    {errors.adminUsername && 'Admin username is required'}
                                </div>
                                <div className="form-group">
                                    <label>Password</label>
                                    <input className="form-control" id="adminPassword" name="adminPassword" ref={register({required: true})}/>
                                    {errors.adminPassword && 'Admin password is required'}
                                </div>
                                <div className="form-group">
                                    <label>Cellphone</label>
                                    <input type="number" min="0" className="form-control" id="adminCellphone" name="adminCellphone" ref={register({required: true})}/>
                                    {errors.adminCellphone && 'Admin cellphone is required'}
                                </div>
                                <div className="form-group">
                                    <label>Email</label>
                                    <input className="form-control" id="adminEmail" name="adminEmail" ref={register({required: true})}/>
                                    {errors.adminEmail && 'Admin email is required'}
                                </div>
                                <div className="form-group">
                                    <label>Address</label>
                                    <input className="form-control" id="adminAddress" name="adminAddress" ref={register({required: true})}/>
                                    {errors.adminAddress && 'Admin address is required'}
                                </div>
                                <div className="form-group">
                                    <label>Photo</label>
                                    <input className="form-control" id="adminPhoto" name="adminPhoto" ref={register({required: true})}/>
                                    {errors.adminPhoto && 'Admin photo is required'}
                                </div>

                                <input className="btn btn-primary float-right" type="submit" value="Add Admin"/>
                            </form>
                        </Card.Body>
                    </Accordion.Collapse>
                </Card>

            </Accordion>

        </div>
    );
};

const mapStateToProps = (state: GlobalState) => ({
    admins: state.admins.admins,
    isFetching: state.admins.isFetching
});

const mapDispatchToProps = (dispatch: any) => {
    return {
        loadAdmins: () => {
            dispatch(fetchAdmins())
        },
        postAdmin: (admin: Admin) => {
            dispatch(postAdmin(admin))
        },
        deleteAdmin: (id: number) => {
            dispatch(deleteAdminRequest(id))
        }
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoAdminList);